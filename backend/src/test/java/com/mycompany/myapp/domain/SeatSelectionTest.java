package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SeatSelectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeatSelectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeatSelection.class);
        SeatSelection seatSelection1 = getSeatSelectionSample1();
        SeatSelection seatSelection2 = new SeatSelection();
        assertThat(seatSelection1).isNotEqualTo(seatSelection2);

        seatSelection2.setId(seatSelection1.getId());
        assertThat(seatSelection1).isEqualTo(seatSelection2);

        seatSelection2 = getSeatSelectionSample2();
        assertThat(seatSelection1).isNotEqualTo(seatSelection2);
    }
}
