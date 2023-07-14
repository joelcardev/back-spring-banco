package br.com.banco;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.banco.services.TransferenciaService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TransferenciaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransferenciaService transferenciaService;

}
