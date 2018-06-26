package com.soundboard.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

public class ObservableList<T> extends Observable implements List<T>{
    private final List<T> delegate = new ArrayList<>();

    public T get(final int index) {
        return delegate.get(index);
    }

    @Override
    public int indexOf(Object object) {
        for (int i=0; i<delegate.size(); i++) {
            if(delegate.get(i).equals(object))
                return i;
        }

        return -1;
    }

    public int size() {
        return delegate.size();
    }

    @NonNull
    @Override
    public List<T> subList(int start, int end) {
        return delegate.subList(start, end);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] array) {
        return delegate.toArray(array);
    }

    public boolean add(final T element) {
        boolean b = delegate.add(element);
        setChanged();
        notifyObservers();
        return b;
    }

    @Override
    public boolean addAll(int location, @NonNull Collection<? extends T> collection) {
        boolean b = delegate.addAll(location, collection);
        setChanged();
        notifyObservers();
        return b;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
        boolean b = delegate.addAll(collection);
        setChanged();
        notifyObservers();
        return b;
    }

    public void add(final int index, final T element) {
        delegate.add(index, element);
        setChanged();
        notifyObservers();
    }

    public T set(final int index, final T element) {
        T o = delegate.set(index, element);
        setChanged();
        notifyObservers();
        return o;
    }

    public T remove(final int index) {
        T o = delegate.remove(index);
        setChanged();
        notifyObservers();
        return o;
    }

    @Override
    public boolean remove(Object object) {
        boolean b = delegate.remove(object);
        setChanged();
        notifyObservers();
        return b;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        boolean b = delegate.removeAll(collection);
        setChanged();
        notifyObservers();
        return b;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        boolean b = delegate.retainAll(collection);
        setChanged();
        notifyObservers();
        return b;
    }

    public void clear() {
        delegate.clear();
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean contains(Object object) {
        return delegate.contains(object);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return delegate.containsAll(collection);
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return delegate.lastIndexOf(object);
    }

    @Override
    public ListIterator<T> listIterator() {
        return delegate.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int location) {
        return delegate.listIterator(location);
    }
}
