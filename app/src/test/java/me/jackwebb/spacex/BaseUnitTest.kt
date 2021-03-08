package me.jackwebb.spacex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

import org.junit.rules.TestRule


open class BaseUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
}