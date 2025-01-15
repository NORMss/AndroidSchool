package com.eltex.androidschool.mvi

import com.eltex.androidschool.view.fragment.post.PostEffect
import com.eltex.androidschool.view.fragment.post.PostMessage
import com.eltex.androidschool.view.fragment.post.PostState

typealias PostStore = Store<PostState, PostMessage, PostEffect>