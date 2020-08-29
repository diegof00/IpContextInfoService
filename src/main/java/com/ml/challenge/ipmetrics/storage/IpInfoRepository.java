package com.ml.challenge.ipmetrics.storage;

import com.ml.challenge.ipmetrics.storage.model.IpInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpInfoRepository extends JpaRepository<IpInfo, Long>  {
}
