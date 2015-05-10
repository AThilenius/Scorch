package com.thilenius.flame.utilities.types;

public class Tuple<T1, T2> {
    public final T1 Key;
    public final T2 Value;
    public Tuple(T1 key, T2 value) {
        Key = key;
        Value = value;
    }
}
