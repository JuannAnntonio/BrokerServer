package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 * The instruments a user has associated with it's pprofile.
 * Created on 15/10/16.
 */
@Entity
@Table(name = "USERXINSTRUMENT", schema = "SIGMACT_BROKER")
public class UserxinstrumentEntity {
    private int idUserxinstrument;
    private int fkIdUser;
    private int fkIdInstrument;
    private int priority;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_userxinstrument", nullable = false)
    public int getIdUserxinstrument() {
        return idUserxinstrument;
    }

    public void setIdUserxinstrument(int idUserxinstrument) {
        this.idUserxinstrument = idUserxinstrument;
    }

    @Basic
    @Column(name = "fk_id_user", nullable = false)
    public int getFkIdUser() {
        return fkIdUser;
    }

    public void setFkIdUser(int fkIdUser) {
        this.fkIdUser = fkIdUser;
    }

    @Basic
    @Column(name = "fk_id_instrument", nullable = false)
    public int getFkIdInstrument() {
        return fkIdInstrument;
    }

    public void setFkIdInstrument(int fkIdInstrument) {
        this.fkIdInstrument = fkIdInstrument;
    }

    @Basic
    @Column(name = "priority", nullable = false)
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserxinstrumentEntity that = (UserxinstrumentEntity) o;

        if (idUserxinstrument != that.idUserxinstrument) return false;
        if (fkIdUser != that.fkIdUser) return false;
        if (fkIdInstrument != that.fkIdInstrument) return false;
        if (priority != that.priority) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idUserxinstrument;
        result = 31 * result + fkIdUser;
        result = 31 * result + fkIdInstrument;
        result = 31 * result + priority;
        return result;
    }
}
