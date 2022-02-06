package com.example.featureTables.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0002\u001b\u001cB0\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012!\u0010\u0006\u001a\u001d\u0012\u0013\u0012\u00110\b\u00a2\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\f0\u0007\u00a2\u0006\u0002\u0010\rJ\b\u0010\u0010\u001a\u00020\u0005H\u0016J\u0018\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0005H\u0016J\u0010\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010\u0017\u001a\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0005H\u0016R,\u0010\u0006\u001a\u001d\u0012\u0013\u0012\u00110\b\u00a2\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\f0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/example/featureTables/domain/TablesAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/featureTables/domain/TablesAdapter$TableViewHolder;", "Landroid/view/View$OnClickListener;", "tablesCount", "", "navigateToOrderFragment", "Lkotlin/Function1;", "Lcom/example/featureTables/domain/TablesAdapter$ViewOwner;", "Lkotlin/ParameterName;", "name", "viewOwner", "", "(ILkotlin/jvm/functions/Function1;)V", "getNavigateToOrderFragment", "()Lkotlin/jvm/functions/Function1;", "getItemCount", "onBindViewHolder", "holder", "position", "onClick", "view", "Landroid/view/View;", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "TableViewHolder", "ViewOwner", "feature-tables_debug"})
public final class TablesAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.featureTables.domain.TablesAdapter.TableViewHolder> implements android.view.View.OnClickListener {
    private final int tablesCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.featureTables.domain.TablesAdapter.ViewOwner, kotlin.Unit> navigateToOrderFragment = null;
    
    public TablesAdapter(int tablesCount, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.featureTables.domain.TablesAdapter.ViewOwner, kotlin.Unit> navigateToOrderFragment) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<com.example.featureTables.domain.TablesAdapter.ViewOwner, kotlin.Unit> getNavigateToOrderFragment() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.example.featureTables.domain.TablesAdapter.TableViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.featureTables.domain.TablesAdapter.TableViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onClick(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/featureTables/domain/TablesAdapter$TableViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/featureTables/databinding/LayoutTableBinding;", "(Lcom/example/featureTables/databinding/LayoutTableBinding;)V", "getBinding", "()Lcom/example/featureTables/databinding/LayoutTableBinding;", "feature-tables_debug"})
    public static final class TableViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.featureTables.databinding.LayoutTableBinding binding = null;
        
        public TableViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.featureTables.databinding.LayoutTableBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.featureTables.databinding.LayoutTableBinding getBinding() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/featureTables/domain/TablesAdapter$ViewOwner;", "", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "getView", "context", "Landroid/content/Context;", "feature-tables_debug"})
    public static final class ViewOwner {
        private final android.view.View view = null;
        
        public ViewOwner(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.View getView(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}