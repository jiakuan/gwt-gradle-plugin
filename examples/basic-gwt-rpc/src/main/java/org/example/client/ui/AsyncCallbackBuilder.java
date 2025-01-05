package org.example.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.function.Consumer;

/**
 * Just a utility class to make the main code a bit prettier.
 */
public final class AsyncCallbackBuilder<T> {
	private final Consumer<T> onSuccess;

	private AsyncCallbackBuilder(final Consumer<T> onSuccess) {
		this.onSuccess = onSuccess;
	}

	static <T> AsyncCallbackBuilder<T> onSuccess(final Consumer<T> onSuccess) {
		return new AsyncCallbackBuilder<>(onSuccess);
	}

	AsyncCallback<T> onFailure(final Consumer<Throwable> onFailure) {
		return new AsyncCallback<T>() {

			@Override
			public void onSuccess(final T result) {
				if(onSuccess != null)
					onSuccess.accept(result);
			}

			@Override
			public void onFailure(final Throwable caught) {
				if(onFailure != null)
					onFailure.accept(caught);
			}
		};
	}
}
