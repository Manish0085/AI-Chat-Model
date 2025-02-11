package com.summerizer.videoSummerizer.Repository;

import com.summerizer.videoSummerizer.Entity.ResumePrompt;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResumePromptRepo extends JpaRepository<ResumePrompt, String> {
}
