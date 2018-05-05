package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 *
 * RolesEntity used by the security.
 * Created on 15/10/16.
 */
@Entity
@Table(name = "ROLES", schema = "SIGMACT_BROKER")
@IdClass(RolesEntityPK.class)
public class RolesEntity {
    private int fkIdUser;
    private String role;

    @Id
    @Column(name = "fk_id_user", nullable = false)
    public int getFkIdUser() {
        return fkIdUser;
    }

    public void setFkIdUser(int fkIdUser) {
        this.fkIdUser = fkIdUser;
    }

    @Id
    @Column(name = "ROLE", nullable = false, length = 10)
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

        RolesEntity that = (RolesEntity) o;

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
