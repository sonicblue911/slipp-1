package net.slipp.domain.qna;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.slipp.domain.fb.EmptyFacebookGroup;
import net.slipp.domain.fb.FacebookGroup;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

public class QuestionDto {
    private static final String FACEBOOK_GROUP_DELIMETER = "::";
    
    private Long questionId;
    
    private String title;

    private String contents;
    
    private String plainTags;
    
    private boolean connected = false;
    
    private String[] plainFacebookGroups;

    private Long originalAnswerId; // 답변에서 질문으로 변환하는 시점의 answerId

    private Long[] moveAnswers; // 답변에서 새 질문으로 분리할 때 같이 분리할 답변 ID 목록
    
    public QuestionDto() {
    }
    
    public QuestionDto(String title, String contents, String plainTags) {
        this(null, title, contents, plainTags);
    }
    
    public QuestionDto(Long questionId, String title, String contents, String plainTags) {
        this.questionId = questionId;
        this.title = title;
        this.contents = contents;
        this.plainTags = plainTags;
    }

    public QuestionDto(Long questionId, Long originalAnswerId, String contents) {
        this.questionId = questionId;
        this.originalAnswerId = originalAnswerId;
        this.contents = contents;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPlainTags() {
        return plainTags;
    }

    public void setPlainTags(String plainTags) {
        this.plainTags = plainTags;
    }
    
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public String[] getPlainFacebookGroups() {
        return plainFacebookGroups;
    }

    public void setPlainFacebookGroups(String[] plainFacebookGroups) {
        this.plainFacebookGroups = plainFacebookGroups;
    }

    public Long getOriginalAnswerId() {
        return originalAnswerId;
    }

    public void setOriginalAnswerId(Long originalAnswerId) {
        this.originalAnswerId = originalAnswerId;
    }

    public Long[] getMoveAnswers() {
        return moveAnswers;
    }

    public void setMoveAnswers(Long[] moveAnswers) {
        this.moveAnswers = moveAnswers;
    }

    public Set<FacebookGroup> getFacebookGroups() {
        return createFacebookGroups(this.plainFacebookGroups);
    }

    static Set<FacebookGroup> createFacebookGroups(String[] fbGroups) {
        if (fbGroups == null || fbGroups.length == 0) {
            return new HashSet<FacebookGroup>();
        }
        
        Set<FacebookGroup> groups = Sets.newHashSet();
        for (String each : fbGroups) {
            groups.add(createFacebookGroup(each));
        }
        return groups;
    }
    
    static FacebookGroup createFacebookGroup(String fbGroup) {
        if (StringUtils.isBlank(fbGroup)) {
            return new EmptyFacebookGroup();
        }
        String[] parsedGroups = fbGroup.split(FACEBOOK_GROUP_DELIMETER);
        if (parsedGroups.length != 2) {
            return new EmptyFacebookGroup();
        }
        return new FacebookGroup(parsedGroups[0], replaceSpaceToDash(parsedGroups[1]));
    }
    
    static String replaceSpaceToDash(String groupName) {
        if (StringUtils.isBlank(groupName)) {
            return "";
        }
        
        return groupName.replaceAll(" ", "-");
    }

    @Override
    public String toString() {
        return "QuestionDto{" +
                "questionId=" + questionId +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", plainTags='" + plainTags + '\'' +
                ", connected=" + connected +
                ", plainFacebookGroups=" + Arrays.toString(plainFacebookGroups) +
                ", originalAnswerId=" + originalAnswerId +
                ", moveAnswers=" + Arrays.toString(moveAnswers) +
                '}';
    }
}
