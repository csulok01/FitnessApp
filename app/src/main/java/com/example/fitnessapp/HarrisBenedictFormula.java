package com.example.fitnessapp;

public class HarrisBenedictFormula {
    private int age;
    private int height;
    private int weight;
    private String gender;
    private String goal;
    private int bmr;

    public HarrisBenedictFormula(int age,int height, int weight, String gender, String goal){
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.goal = goal;
        calculateBmr();
    }
    public void calculateBmr(){
        if(gender.equals("férfi")){
            bmr = (int)((10*weight) + (6.25*height) - (5*age) +5) + 300;
        }else if(gender.equals("nő")){
            bmr = (int)((10*weight) + (6.25*height) - (5*age) - 161) + 350;
        }
        if(goal.equals("tömegnövelés")){
            bmr+=400;
        }else if(goal.equals("fogyás")){
            bmr-=500;
        }
    }

    public int getBmr() {
        return bmr;
    }
}
