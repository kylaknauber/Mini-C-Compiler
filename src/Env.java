// Name: Kyla Knauber
// Course: CMPSC 470
// Part 2 Due: April 30, 2025
// Homework 5: Semantic Checker

import java.util.ArrayList;
import java.util.HashMap;

public class Env
{
    public Env prev;
    public HashMap<String, Object> symTable;
    public Env(Env prev)
    {
        HashMap<String, Object> symbolTable = new HashMap<>();
        this.symTable = symbolTable;
        if(prev != null) {
            this.prev = prev;
        }
        else {
            this.prev = null;
        }

    }
    public void Put(String name, Object value)
    {
        this.symTable.put(name, value);
    }
    public Object Get(String name)
    {
        Env current = this;
        while (current != null) {
            if (current.symTable.containsKey(name)) {
                return current.symTable.get(name);
            }
            current = current.prev;
        }
        return null;
    }
}
