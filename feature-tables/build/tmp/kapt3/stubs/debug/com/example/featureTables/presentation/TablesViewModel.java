package com.example.featureTables.presentation;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0006J\u0006\u0010\u0015\u001a\u00020\u0013R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R*\u0010\t\u001a\u0012\u0012\u000e\u0012\f\u0012\u0004\u0012\u00020\u00060\u0005j\u0002`\u000b0\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\r\u00a8\u0006\u0016"}, d2 = {"Lcom/example/featureTables/presentation/TablesViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_onTableSelected", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/waiterCore/domain/tools/Event;", "Lcom/example/featureTables/domain/TablesAdapter$ViewOwner;", "_state", "Lcom/example/featureTables/presentation/TablesVMStates;", "onTableSelected", "Landroidx/lifecycle/LiveData;", "Lcom/example/featureTables/presentation/OnTableClickEvent;", "getOnTableSelected", "()Landroidx/lifecycle/LiveData;", "setOnTableSelected", "(Landroidx/lifecycle/LiveData;)V", "state", "getState", "onTableClick", "", "viewOwner", "resetState", "feature-tables_debug"})
public final class TablesViewModel extends androidx.lifecycle.ViewModel {
    private androidx.lifecycle.MutableLiveData<com.example.featureTables.presentation.TablesVMStates> _state;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.featureTables.presentation.TablesVMStates> state = null;
    private androidx.lifecycle.MutableLiveData<com.example.waiterCore.domain.tools.Event<com.example.featureTables.domain.TablesAdapter.ViewOwner>> _onTableSelected;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.LiveData<com.example.waiterCore.domain.tools.Event<com.example.featureTables.domain.TablesAdapter.ViewOwner>> onTableSelected;
    
    public TablesViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.featureTables.presentation.TablesVMStates> getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.waiterCore.domain.tools.Event<com.example.featureTables.domain.TablesAdapter.ViewOwner>> getOnTableSelected() {
        return null;
    }
    
    public final void setOnTableSelected(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LiveData<com.example.waiterCore.domain.tools.Event<com.example.featureTables.domain.TablesAdapter.ViewOwner>> p0) {
    }
    
    public final void onTableClick(@org.jetbrains.annotations.NotNull()
    com.example.featureTables.domain.TablesAdapter.ViewOwner viewOwner) {
    }
    
    public final void resetState() {
    }
}