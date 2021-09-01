package xrm.extrim.planner.parser;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
public class HTMLParser {
    public JSONObject tableToJson(String html) throws JSONException {
        Document doc = Jsoup.parse(html);
        JSONObject  jsonParentObject = new JSONObject ();
        JSONObject jsonObject;
        for (Element table : doc.select("table")) {
            for (Element row : table.select("tr")) {
                jsonObject = new JSONObject();
                Elements tds = row.select("td");
                if(tds.size() > 0) {
                    jsonObject.put("fullName", tds.get(0).text());
                    jsonObject.put("position", tds.get(1).text());
                    jsonObject.put("department", tds.get(2).text());
                    jsonObject.put("office", tds.get(3).text());
                    jsonObject.put("skype", tds.get(4).text());
                    jsonObject.put("mail", tds.get(5).text());
                    jsonObject.put("InteriorNumber", tds.get(6).text());
                    jsonObject.put("mobileNumber", tds.get(7).text());
                    jsonObject.put("birthday", tds.get(9).text());
                    jsonParentObject.put(tds.get(5).text(), jsonObject);
                }
            }
        }
        return jsonParentObject;
    }
}
