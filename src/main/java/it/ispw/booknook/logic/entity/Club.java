package it.ispw.booknook.logic.entity;

public class Club {
    private String name;
    private Long numMembers;
    private String description;

    public Club(String name, Long numMembers, String description){
        this.name = name;
        this.numMembers = numMembers;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(Long numMembers) {
        this.numMembers = numMembers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
