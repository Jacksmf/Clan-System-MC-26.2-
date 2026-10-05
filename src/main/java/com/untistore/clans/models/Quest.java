package com.untistore.clans.models;

import org.bukkit.Material;

import java.util.List;

public class Quest {
    private String questId;
    private String type;
    private String target;
    private Material displayItem;
    private String description;
    private int requiredAmount;
    private String rewardType; // e.g., "COMMAND"
    private List<String> rewards;

    private int progressAmount = 0;

    public Quest(String questId, String type, String target, Material displayItem, String description, int requiredAmount, String rewardType, List<String> rewards) {
        this.questId = questId;
        this.type = type;
        this.target = target;
        this.displayItem = displayItem;
        this.description = description;
        this.requiredAmount = requiredAmount;
        this.rewardType = rewardType;
        this.rewards = rewards;
    }

    public String getQuestId() { return questId; }

    public String getType() { return type; }
    public String getTarget() { return target; }
    public Material getDisplayItem() { return displayItem; }

    public String getDescription() { return description; }

    public int getRequiredAmount() { return requiredAmount; }

    public String getRewardType() { return rewardType; }

    public List<String> getRewards() { return rewards; }

    public int getProgressAmount() { return progressAmount; }
    public void setProgressAmount(int progressAmount) { this.progressAmount = progressAmount; }

    public boolean isCompleted() {
        return this.progressAmount >= this.requiredAmount;
    }
}