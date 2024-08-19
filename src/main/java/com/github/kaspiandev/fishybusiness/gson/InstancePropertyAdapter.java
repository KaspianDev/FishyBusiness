package com.github.kaspiandev.fishybusiness.gson;

import com.google.gson.InstanceCreator;

public interface InstancePropertyAdapter<T> extends PropertyAdapter<T>, InstanceCreator<T> {

}
