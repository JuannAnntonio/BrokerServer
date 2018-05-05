package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 *
 * The aggresion status is used to know what is happening in the aggresion.
 *
 * Created on 15/10/16.
 */
@Entity
@Table(name = "AGGRESSION_STATUS", schema = "SIGMACT_BROKER")
public class AggressionStatusEntity {
    private int idTransactionStatus;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_transaction_status", nullable = false)
    public int getIdTransactionStatus() {
        return idTransactionStatus;
    }

    public void setIdTransactionStatus(int idTransactionStatus) {
        this.idTransactionStatus = idTransactionStatus;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AggressionStatusEntity that = (AggressionStatusEntity) o;

        if (idTransactionStatus != that.idTransactionStatus) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idTransactionStatus;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
