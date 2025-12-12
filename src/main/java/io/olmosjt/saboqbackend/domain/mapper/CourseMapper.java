package io.olmosjt.saboqbackend.domain.mapper;

import io.olmosjt.saboqbackend.domain.dto.CourseDto;
import io.olmosjt.saboqbackend.domain.dto.LessonDto;
import io.olmosjt.saboqbackend.domain.entity.Course;
import io.olmosjt.saboqbackend.domain.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CourseMapper {

    /**
     * Maps a Course entity to a lightweight Summary DTO.
     * Includes AuthorInfo but no Lessons.
     */
    public static CourseDto.Summary toSummary(Course course) {
        return new CourseDto.Summary(
                course.getId(),
                toAuthorInfo(course.getAuthor()),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnailUrl(),
                course.getStatus(),
                course.isPublished(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }

    /**
     * Maps a Course entity to a full Detail DTO.
     * Requires the caller to provide the mapped list of lessons.
     */
    public static CourseDto.Detail toDetail(Course course, List<LessonDto.LessonSummaryResponse> lessons) {
        return new CourseDto.Detail(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnailUrl(),
                course.getStatus(),
                course.isPublished(),
                toAuthorInfo(course.getAuthor()),
                lessons,
                course.getCreatedAt()
        );
    }

    /**
     * Helper to extract AuthorInfo from User entity.
     * safely handles potential nulls for Avatar.
     */
    private static CourseDto.AuthorInfo toAuthorInfo(User author) {
        if (author == null) return null;

        String avatarUrl = (author.getChosenAvatar() != null)
                ? author.getChosenAvatar().getPath()
                : null;

        return new CourseDto.AuthorInfo(
                author.getId(),
                author.getEmail(),
                author.getUsername(),
                author.getFirstName(),
                author.getLastName(),
                avatarUrl
        );
    }

}
