package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contact")
@Data
@EqualsAndHashCode(of = {"id"})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Contact implements Serializable {
    private static final long serialVersionUID = 1057224550932226362L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @GenericGenerator(name = "seq", strategy = "increment")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "office")
    private String office;

    @Type(type = "jsonb")
    @Column(columnDefinition = "messengers")
    private Messengers messengers;

    @JsonBackReference("user-contact")
    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}
