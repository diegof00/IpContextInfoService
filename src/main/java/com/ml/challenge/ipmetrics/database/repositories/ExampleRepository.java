package com.ml.challenge.ipmetrics.database.repositories;

import com.ml.challenge.ipmetrics.database.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExampleRepository extends JpaRepository<Example, Long>  {
}
