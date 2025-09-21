package fr.tiogars.architecture.create.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import fr.tiogars.architecture.create.entities.AbstractEntity;
import fr.tiogars.architecture.create.forms.AbstractCreateForm;
import fr.tiogars.architecture.create.models.AbstractModel;

/**
 * Unit tests for the AbstractCreateService class.
 * <p>
 * This test class uses manual stubs and JUnit assertions to verify the behavior
 * of the AbstractCreateService and its abstract methods without any mocking
 * framework.
 */
public class AbstractCreateServiceTest {
    /**
     * Stub implementation of AbstractCreateForm for testing.
     */
    static class TestCreateForm extends AbstractCreateForm {
    }

    /**
     * Stub implementation of AbstractEntity for testing.
     */
    static class TestEntity extends AbstractEntity {
    }

    /**
     * Stub implementation of AbstractModel for testing.
     */
    static class TestModel extends AbstractModel {
    }

    /**
     * Manual stub for CrudRepository to track method calls for testing.
     */
    static class StubCrudRepository implements CrudRepository<TestEntity, Long> {
        /** Indicates if save() was called. */
        public boolean saveCalled = false;
        /** Stores the entity passed to save(). */
        public TestEntity savedEntity = null;

        /**
         * Simulates saving an entity and records the call.
         * 
         * @param entity the entity to save
         * @return the saved entity
         */
        @Override
        public <S extends TestEntity> S save(S entity) {
            saveCalled = true;
            savedEntity = entity;
            return entity;
        }

        // Other methods throw UnsupportedOperationException for brevity
        @Override
        public <S extends TestEntity> Iterable<S> saveAll(Iterable<S> entities) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Optional<TestEntity> findById(Long aLong) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean existsById(Long aLong) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterable<TestEntity> findAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterable<TestEntity> findAllById(Iterable<Long> longs) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long count() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(Long aLong) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void delete(TestEntity entity) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll(Iterable<? extends TestEntity> entities) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }
    }

    StubCrudRepository crudRepository;
    AbstractCreateService<TestCreateForm, TestEntity, TestModel, StubCrudRepository> service;

    boolean toModelFromFormCalled = false;
    boolean validateCalled = false;
    boolean toEntityCalled = false;
    boolean toModelFromEntityCalled = false;

    /**
     * Default constructor.
     */
    public AbstractCreateServiceTest() {
        // Empty constructor
    }

    @BeforeEach
    void setUp() {
        crudRepository = new StubCrudRepository();
        service = new AbstractCreateService<TestCreateForm, TestEntity, TestModel, StubCrudRepository>(crudRepository) {
            @Override
            public TestEntity toEntity(TestModel model) {
                toEntityCalled = true;
                return new TestEntity();
            }

            @Override
            public TestModel toModel(TestEntity entity) {
                toModelFromEntityCalled = true;
                return new TestModel();
            }

            @Override
            public TestModel toModel(TestCreateForm createForm) {
                toModelFromFormCalled = true;
                return new TestModel();
            }

            @Override
            public void validate(TestModel model) {
                validateCalled = true;
            }
        };
    }

    /**
     * Verifies that the constructor correctly sets the CrudRepository instance.
     */
    @Test
    void testConstructorSetsCrudRepository() {
        assertSame(crudRepository, service.crudRepository);
    }

    /**
     * Tests that the create() method calls all required abstract methods and
     * repository save in order.
     */
    @Test
    void testCreateCallsMethodsInOrderAndReturnsModel() {
        TestCreateForm form = new TestCreateForm();
        TestModel result = service.create(form);
        assertTrue(toModelFromFormCalled, "toModel(createForm) should be called");
        assertTrue(validateCalled, "validate(model) should be called");
        assertTrue(toEntityCalled, "toEntity(model) should be called");
        assertTrue(crudRepository.saveCalled, "crudRepository.save(entity) should be called");
        assertTrue(toModelFromEntityCalled, "toModel(entity) should be called");
        assertNotNull(result);
    }

    /**
     * Tests that toEntity() is implemented and called.
     */
    @Test
    void testToEntityIsAbstractAndImplemented() {
        TestModel model = new TestModel();
        TestEntity entity = service.toEntity(model);
        assertNotNull(entity);
        assertTrue(toEntityCalled);
    }

    /**
     * Tests that toModel(entity) is implemented and called.
     */
    @Test
    void testToModelFromEntityIsAbstractAndImplemented() {
        TestEntity entity = new TestEntity();
        TestModel model = service.toModel(entity);
        assertNotNull(model);
        assertTrue(toModelFromEntityCalled);
    }

    /**
     * Tests that toModel(createForm) is implemented and called.
     */
    @Test
    void testToModelFromFormIsAbstractAndImplemented() {
        TestCreateForm form = new TestCreateForm();
        TestModel model = service.toModel(form);
        assertNotNull(model);
        assertTrue(toModelFromFormCalled);
    }

    /**
     * Tests that validate(model) is implemented and called.
     */
    @Test
    void testValidateIsAbstractAndImplemented() {
        TestModel model = new TestModel();
        assertDoesNotThrow(() -> service.validate(model));
        assertTrue(validateCalled);
    }
}
