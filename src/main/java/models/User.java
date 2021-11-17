package models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Task> tasks = new ArrayList<>();


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                (name == user.name || (name != null && name.equals(user.getName())));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString()
    {
        return "ID: " + getId() + ", " +
                "Name: " + getName() + ", " +
                "Tasks: " + getTasks().size();
    }
}
