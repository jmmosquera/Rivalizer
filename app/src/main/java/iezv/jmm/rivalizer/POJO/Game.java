package iezv.jmm.rivalizer.POJO;



public class Game {
    private int idGame;
    private String cloudId;
    private String name;
    private String urlPhoto;
    private String description;
    private String rules;

    public Game () {}

    public Game(int idGame, String cloudId, String name, String urlPhoto, String description, String rules) {
        this.idGame = idGame;
        this.cloudId = cloudId;
        this.name = name;
        this.urlPhoto = urlPhoto;
        this.description = description;
        this.rules = rules;
    }

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }
}
