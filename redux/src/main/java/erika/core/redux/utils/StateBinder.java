package erika.core.redux.utils;

import erika.core.redux.Component;

public class StateBinder<AppState> {

    private final LinkedList<ComponentWithState<AppState, ?>> childComponents = new LinkedList<>();

    public void onComponentAdded(Component<AppState, ?> component) {
        childComponents.add(new ComponentWithState<>(component));
    }

    public void onComponentRemoved(final Component component) {
        childComponents.removeWhere(new Predicate<ComponentWithState<AppState, ?>>() {
            @Override
            public boolean test(ComponentWithState<AppState, ?> x) {
                return x.component == component;
            }
        });
    }

    public void rebindState(AppState appState) {
        for (ComponentWithState<AppState, ?> child : childComponents) {
            child.bind(appState);
        }
    }

    private static class ComponentWithState<AppState, State> {
        final Component<AppState, State> component;

        private ComponentWithState(Component<AppState, State> component) {
            this.component = component;
        }

        private void bind(AppState state) {
            State oldState = component.getState();
            State newState = component.getStateFromStore(state);
            if (newState != oldState) {
                component.setState(newState);
                if (component.isAlive()) {
                    component.bindStateToView(newState);
                }
            }
        }
    }

}
