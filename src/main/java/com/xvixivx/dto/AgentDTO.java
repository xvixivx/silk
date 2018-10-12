package com.xvixivx.dto;

import java.util.List;

public class AgentDTO {

    private String displayName;
    private String role;
    private String weapon;
    private String alternateAction;
    private String ability;
    private String ultimateAbility;
    private int damage;
    private int health;
    private String image;
    private List<String> skins;

    public AgentDTO(String displayName,
                    String role,
                    String weapon,
                    String alternateAction,
                    String ability,
                    String ultimateAbility,
                    int damage,
                    int health,
                    String image)
    {
        this.displayName = displayName;
        this.role = role;
        this.weapon = weapon;
        this.alternateAction = alternateAction;
        this.ability = ability;
        this.ultimateAbility = ultimateAbility;
        this.damage = damage;
        this.health = health;
        this.image = image;
    }

    public String getDisplayName() { return displayName; }

    public String getRole() {
        return role;
    }

    public String getWeapon() {
        return weapon;
    }

    public String getAlternateAction() { return alternateAction; }

    public String getAbility() {
        return ability;
    }

    public String getUltimateAbility() {
        return ultimateAbility;
    }

    public int getDamage() {
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public String getImage() {
        return image;
    }

    public List<String> getSkins() {
        return skins;
    }
}
