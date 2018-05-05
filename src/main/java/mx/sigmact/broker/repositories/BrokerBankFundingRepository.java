package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.BankFundingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Calendar;

/**
 * Created by norberto on 28/02/2017.
 */
public interface BrokerBankFundingRepository extends CrudRepository<BankFundingEntity, Integer>{
    BankFundingEntity findOneByDate(Calendar date);
}
