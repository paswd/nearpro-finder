package ru.paswd.nearprofinder.model;

import java.util.List;

import ru.paswd.nearprofinder.PropertyItem;

public interface OnPropertyUpdateListener {
    void onUpdate(List<PropertyItem> list);
}
