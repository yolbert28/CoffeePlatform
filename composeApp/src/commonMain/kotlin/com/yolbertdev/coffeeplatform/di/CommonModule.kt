package com.yolbertdev.coffeeplatform.di

import com.yolbertdev.coffeeplatform.data.database.dao.CustomerDao
import com.yolbertdev.coffeeplatform.data.database.repository.CustomerRepositoryImpl
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.repository.CustomerRepository
import com.yolbertdev.coffeeplatform.domain.usecase.InsertCustomerUseCase
import com.yolbertdev.coffeeplatform.domain.usecase.SelectAllCustomerUseCase
import com.yolbertdev.coffeeplatform.presentation.ReportViewModel
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.CustomerScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.add.AddCustomerScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.home.HomeScreenModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import com.yolbertdev.coffeeplatform.data.database.dao.LoanDao
import com.yolbertdev.coffeeplatform.data.database.dao.PaymentDao
import com.yolbertdev.coffeeplatform.data.database.repository.LoanRepositoryImpl
import com.yolbertdev.coffeeplatform.data.database.repository.PaymentRepositoryImpl
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import com.yolbertdev.coffeeplatform.domain.usecase.InsertLoanUseCase
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.detail.CustomerDetailScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.LoanScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.add.AddLoanScreenModel

val commonModule = module {
    single<CoffeeDatabase> { CoffeeDatabase(get()) }
    single<CustomerDao> { CustomerDao(get()) }
    single<LoanDao> { LoanDao(get()) }
    single<PaymentDao> { PaymentDao(get()) }
    single<HomeScreenModel> { HomeScreenModel() }
    single<CustomerScreenModel>{CustomerScreenModel(get(), get())}
    single { InsertLoanUseCase(get()) }

    factory<AddCustomerScreenModel>{ AddCustomerScreenModel(get())}
    factory { AddLoanScreenModel(get(), get()) }
    single<InsertCustomerUseCase>{InsertCustomerUseCase(get())}
    single<SelectAllCustomerUseCase>{SelectAllCustomerUseCase(get())}
    singleOf(::CustomerRepositoryImpl) bind CustomerRepository::class
    singleOf(::LoanRepositoryImpl) bind LoanRepository::class
    singleOf(::PaymentRepositoryImpl) bind PaymentRepository::class
    factory { CustomerDetailScreenModel(get()) }
    factory { LoanScreenModel(get()) }
    factory { ReportViewModel(get(),get(),get()) }}
