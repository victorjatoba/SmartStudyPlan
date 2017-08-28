package br.com.smartstudyplan.bean;

/**
 * Esta classe encapsula as informações referentes a disponibilidade padrões.
 */
public class DefaultAvailability extends Availability {

    private String name;
    private String description;

    public DefaultAvailability(){
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", description: " + description + ", " + super.toString();
    }
}
