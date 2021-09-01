package xrm.extrim.planner.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xrm.extrim.planner.converter.FileTypeCodeConverter;
import xrm.extrim.planner.enums.FileType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "file")
@Data
@EqualsAndHashCode(callSuper = true)
public class File extends BaseEntity {

    @NotNull
    @Column(name = "upload_date")
    private LocalDate uploadDate;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "blob")
    private byte[] blob;

    @NotNull
    @Column(name = "file_type_code")
    @Convert(converter = FileTypeCodeConverter.class)
    private FileType fileType;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_file",
            joinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> attachedUsers = new ArrayList<>();

}
