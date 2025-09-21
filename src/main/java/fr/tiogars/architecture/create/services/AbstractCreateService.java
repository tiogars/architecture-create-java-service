package fr.tiogars.architecture.create.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import fr.tiogars.architecture.create.entities.AbstractEntity;
import fr.tiogars.architecture.create.forms.AbstractCreateForm;

/**
 * Abstract class for create services.
 * This class can be extended to implement specific create operations
 * for different entities.
 *
 * @param <CreateForm> the type of the form used to create an entity
 * @param <Entity> the type of the entity being created
 * @param <Model> the type of the model used in the service
 * @param <CreateRepository> the type of the repository used for CRUD operations
 */
public abstract class AbstractCreateService<CreateForm extends AbstractCreateForm, Entity extends AbstractEntity, Model, CreateRepository extends CrudRepository<Entity, Long>> {

    /**
     * The CrudRepository used for database operations.
     * This repository should be injected in the constructor
     * and is used to perform CRUD operations
     * on the entity type.
     */
    public CrudRepository<Entity, Long> crudRepository;

    /**
     * Logger for this service.
     * Used to log information about operations performed by the service.
     */
    private final Logger logger = LoggerFactory.getLogger(AbstractCreateService.class);

    /**
     * Default constructor for the AbstractCreateService.
     * This constructor is provided for cases where dependency injection
     * is not used.
     */
    public AbstractCreateService() {
        // Empty constructor
    }

    /**
     * Constructor for the AbstractCreateService.
     * Initializes the CrudRepository used for database operations.
     * 
     * @param crudRepository the CrudRepository for the entity
     */
    public AbstractCreateService(CreateRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    /**
     * Converts a CreateForm to an Entity.
     * This method should be implemented by subclasses to define how
     * the conversion is done.
     * 
     * @param model the model containing data to create the entity
     * @return the entity created from the model
     */
    public abstract Entity toEntity(Model model);

    /**
     * Converts an UpdateRequest to an Entity.
     * This method should be implemented by subclasses to define how
     * the conversion is done.
     * 
     * @param entity the entity
     * @return the model
     */
    public abstract Model toModel(Entity entity);

    /**
     * Converts a CreateForm to a Model.
     * This method should be implemented by subclasses to define how
     * the conversion is done.
     * 
     * @param createForm the form containing data to create the model
     * @return the model created from the form
     */
    public abstract Model toModel(CreateForm createForm);

    /**
     * Validates the provided model.
     * This method should be implemented by subclasses to define how
     * the validation is done.
     * 
     * @param model the model to validate
     */
    public abstract void validate(Model model);

    /**
     * Creates a new entity based on the provided CreateForm.
     *
     * @param createForm the form containing data to create the entity
     * @return a response containing the ID of the created entity
     */
    public Model create(CreateForm createForm) {

        logger.info("Creating entity with data: {}", createForm);

        Model model = toModel(createForm);

        validate(model);

        Entity entity = toEntity(model);

        Entity savedEntity = crudRepository.save(entity);

        return toModel(savedEntity);
    }
}