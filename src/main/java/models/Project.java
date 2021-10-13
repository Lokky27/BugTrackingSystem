package models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "projects")
public class Project
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date deadLine;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Task> tasks = new ArrayList<>();

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Project project = (Project) o;
        return id != null && Objects.equals(id, project.id);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
