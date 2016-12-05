package com.lujianzhao.base.frame.presenter.factory;

import com.lujianzhao.base.frame.presenter.BasePresenter;

import java.util.HashMap;

/**
 * This is the singleton where all presenters are stored.
 */
public enum PresenterStorage {

    INSTANCE;

    private HashMap<String, BasePresenter> mIdToPresenter = new HashMap<>();
    private HashMap<BasePresenter, String> mPresenterToId = new HashMap<>();

    /**
     * Adds a presenter to the storage
     *
     * @param presenter a presenter to add
     */
    public void add(final BasePresenter presenter) {
        String id = presenter.getClass().getSimpleName() + "/" + System.nanoTime() + "/" + (int)(Math.random() * Integer.MAX_VALUE);
        mIdToPresenter.put(id, presenter);
        mPresenterToId.put(presenter, id);
        presenter.addOnDestroyListener(new BasePresenter.OnDestroyListener() {
            @Override
            public void onDestroy() {
                mIdToPresenter.remove(mPresenterToId.remove(presenter));
                presenter.removeOnDestroyListener(this);
            }
        });
    }

    /**
     * Returns a presenter by id or null if such presenter does not exist.
     *
     * @param id  id of a presenter that has been received by calling {@link #getId(BasePresenter)}
     * @param <P> a type of presenter
     * @return a presenter or null
     */
    public <P> P getPresenter(String id) {
        //noinspection unchecked
        return (P) mIdToPresenter.get(id);
    }

    /**
     * Returns id of a given presenter.
     *
     * @param presenter a presenter to get id for.
     * @return if of the presenter.
     */
    public String getId(BasePresenter presenter) {
        return mPresenterToId.get(presenter);
    }

    /**
     * Removes all presenters from the storage.
     * Use this method for testing purposes only.
     */
    public void clear() {
        mIdToPresenter.clear();
        mPresenterToId.clear();
    }
}
