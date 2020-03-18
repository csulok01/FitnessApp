package com.example.fitnessapp;

class HistoryTableData {


    private String date;
    private int age;
    private int height;
    private int weight;
    private int currentWeight;
    private int waterDrunken;
    private String goal;
    private String gender;
    private String foodEaten;
    private String sportsDone;

    public HistoryTableData() { }


    public String getDate() { return date; }

    public int getAge() { return age; }

    public int getHeight() { return height; }

    public int getWeight() { return weight; }

    public int getCurrentWeight() { return currentWeight; }

    public int getWaterDrunken() { return waterDrunken; }

    public String getGoal() { return goal; }

    public String getGender() { return gender; }

    public String getFoodEaten() { return foodEaten; }

    public String getSportsDone() { return sportsDone; }



    public void setDate(String date) { this.date = date; }

    public void setAge(int age) { this.age = age; }

    public void setHeight(int height) { this.height = height; }

    public void setWeight(int weight) { this.weight = weight; }

    public void setCurrentWeight(int currentWeight) { this.currentWeight = currentWeight; }

    public void setWaterDrunken(int waterDrunken) { this.waterDrunken = waterDrunken; }

    public void setGender(String gender) { this.gender = gender; }

    public void setFoodEaten(String foodEaten) { this.foodEaten = foodEaten; }

    public void setGoal(String goal) { this.goal = goal; }

    public void setSportsDone(String sportsDone) { this.sportsDone = sportsDone; }
}
