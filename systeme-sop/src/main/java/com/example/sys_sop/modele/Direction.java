package com.example.sys_sop.modele;

import java.util.Objects;

public class Direction {
    
    private String codeDirection;
    private String libelleDirection;
    private String codeAbrev;

    public Direction() {
    }

    public Direction(String codeDirection, String libelleDirection, String codeAbrev) {
        this.codeDirection = codeDirection;
        this.libelleDirection = libelleDirection;
        this.codeAbrev = codeAbrev;
    }

    public String getCodeDirection() {
        return codeDirection;
    }

    public String getLibelleDirection() {
        return libelleDirection;
    }

    public String getCodeAbrev() {
        return codeAbrev;
    }
    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof Direction)) return  false;
        Direction direction =(Direction) o;
        return  Objects.equals(codeDirection, direction.codeDirection);
    } 
    @Override 
    public int hashCode(){
        return Objects.hash(codeDirection);
    }

    @Override
    public String toString() {
        return "Direction{" +
                "codeDirection='" + codeDirection + '\'' +
                ", libelleDirection='" + libelleDirection + '\'' +
                ", codeAbrev='" + codeAbrev + '\'' +
                '}';
    }
    
}
