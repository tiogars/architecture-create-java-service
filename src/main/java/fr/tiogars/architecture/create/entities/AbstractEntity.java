/**
 * Represents an abstract entity with a unique identifier.
 */
package fr.tiogars.architecture.create.entities;

/**
 * Base class for entities with an ID field.
 */
public class AbstractEntity {

    /**
     * Constructor for AbstractEntity.
     */
    public AbstractEntity() {
        // Empty constructor
    }

    /**
     * The unique identifier for the entity.
     */
    private Long id;

    /**
     * Sets the unique identifier for the entity.
     *
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the unique identifier of the entity.
     *
     * @return the unique identifier
     */
    public Long getId() {
        return id;
    }
}
