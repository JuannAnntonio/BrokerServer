package mx.sigmact.broker.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Roles primary key
 * Created on 15/10/16.
 */
public class RolesEntityPK implements Serializable {
    private int fkIdUser;
    private String role;

    @Column(name = "fk_id_user", nullable = false)
    @Id
    public int getFkIdUser() {
        return fkIdUser;
    }

    public void setFkIdUser(int fkIdUser) {
        this.fkIdUser = fkIdUser;
    }

    @Column(name = "ROLE", nullable = false, length = 10)
    @Id
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RolesEntityPK that = (RolesEntityPK) o;

        if (fkIdUser != that.fkIdUser) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fkIdUser;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
