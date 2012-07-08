package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
	@Mock
	private QuestionRepository questionRepository;
	
	@Mock
	private AnswerRepository answerRepository;
	
	@InjectMocks
	private QnaService dut = new QnaService();
	
	@Test
	public void updateQuestion_sameUser() {
		// given
		SocialUser loginUser = new SocialUser(10);
		Question question = new Question(1L);
		question.writedBy(loginUser);
		question.setPlainTags("");
		when(questionRepository.findOne(question.getQuestionId())).thenReturn(question);
		
		// when
		dut.updateQuestion(loginUser, question);
		
		// then
		verify(questionRepository).save(question);
	}
	
	@Test (expected=AccessDeniedException.class)
	public void updateQuestion_differentUser() {
		// given
		SocialUser loginUser = new SocialUser(10);
		Question question = new Question(1L);
		question.writedBy(new SocialUser(11));
		question.setPlainTags("");
		when(questionRepository.findOne(question.getQuestionId())).thenReturn(question);
		
		// when
		dut.updateQuestion(loginUser, question);
	}
	
	@Test
	public void deleteAnswer_sameUser() throws Exception {
		// given
		SocialUser loginUser = new SocialUser(10);
		Answer answer = new Answer(2L);
		answer.writedBy(loginUser);
		Question question = new Question(1L);
		question.setAnswerCount(5);
		when(answerRepository.findOne(answer.getAnswerId())).thenReturn(answer);
		when(questionRepository.findOne(question.getQuestionId())).thenReturn(question);
		
		// when
		dut.deleteAnswer(loginUser, question.getQuestionId(), answer.getAnswerId());
		
		// then
		verify(answerRepository).delete(answer);
		assertThat(question.getAnswerCount(), is(4));
	}
	
	@Test (expected=AccessDeniedException.class)
	public void deleteAnswer_differentUser() throws Exception {
		// given
		SocialUser loginUser = new SocialUser(10);
		Long questionId = 1L;
		Answer answer = new Answer(2L);
		answer.writedBy(new SocialUser(11));
		when(answerRepository.findOne(answer.getAnswerId())).thenReturn(answer);
		
		// when
		dut.deleteAnswer(loginUser, questionId, answer.getAnswerId());
	}
}
