package org.hibernate.orm.test.annotations.id.generators.entity;

import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.TableGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SessionFactory
@DomainModel(
		annotatedClasses = {
				ClassLevelGeneratorTest.EntityWithAnonSequenceGenerator.class,
				ClassLevelGeneratorTest.EntityWithAnonTableGenerator.class
		}
)
public class ClassLevelGeneratorTest {
	@Test
	void testAnonGenerator(SessionFactoryScope scope) {
		// this won't work with global scoping due to
		assertThat( scope.getSessionFactory().getSessionFactoryOptions().getJpaCompliance().isGlobalGeneratorScopeEnabled() ).isFalse();

		scope.inSession(s-> {
			EntityWithAnonSequenceGenerator entity1 = new EntityWithAnonSequenceGenerator();
			EntityWithAnonTableGenerator entity2 = new EntityWithAnonTableGenerator();
			s.persist(entity1);
			s.persist(entity2);
			assertEquals(42, entity1.id);
			assertEquals(70, entity2.id);
		});
	}
	@Entity(name = "EntityWithAnonSequenceGenerator")
	@SequenceGenerator(initialValue = 42)
	static class EntityWithAnonSequenceGenerator {
		@Id
		@GeneratedValue
		long id;
	}
	@Entity(name = "EntityWithAnonTableGenerator")
	@TableGenerator(initialValue = 69)
	static class EntityWithAnonTableGenerator {
		@Id
		@GeneratedValue
		long id;
	}
}
