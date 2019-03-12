package mu.inovus.entity;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by maratgatin on 13/03/2019.
 */
public class RegistrationNumberTest {

    @Test
    public void toStringOutput() throws Exception {
        assertThat(new RegistrationNumber(12, 13).toString(), is("А012ЕЕ 116 RUS"));
    }

}