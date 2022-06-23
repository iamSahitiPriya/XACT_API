package unit.com.xact.assessment.services;

import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.repositories.AssessmentTopicRepository;
import com.xact.assessment.services.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TopicServiceTest {
    private TopicService topicService;
    private AssessmentTopicRepository assessmentTopicRepository;

    @BeforeEach
    public void beforeEach() {
        assessmentTopicRepository = mock(AssessmentTopicRepository.class);
        topicService = new TopicService(assessmentTopicRepository);
    }

    @Test
    public void shouldGetDetailsForParticularTopicId() {
        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic Name");

        when(assessmentTopicRepository.findById(topicId)).thenReturn(Optional.of(assessmentTopic));
        Optional<AssessmentTopic> actualTopic = topicService.getTopic(topicId);

        assertEquals(assessmentTopic.getTopicId(), actualTopic.get().getTopicId());
        assertEquals(assessmentTopic.getTopicName(), actualTopic.get().getTopicName());

    }

}


