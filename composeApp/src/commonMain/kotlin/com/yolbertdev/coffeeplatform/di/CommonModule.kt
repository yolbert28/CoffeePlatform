package com.yolbertdev.coffeeplatform.di

import com.yolbertdev.coffeeplatform.data.database.dao.CustomerDao
import com.yolbertdev.coffeeplatform.data.database.repository.CustomerRepositoryImpl
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.repository.CustomerRepository
import com.yolbertdev.coffeeplatform.domain.usecase.*
import com.yolbertdev.coffeeplatform.presentation.ReportViewModel
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.CustomerScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.add.AddCustomerScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.home.HomeScreenModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import com.yolbertdev.coffeeplatform.data.database.dao.LoanDao
import com.yolbertdev.coffeeplatform.data.database.dao.PaymentDao
import com.yolbertdev.coffeeplatform.data.database.dao.SyncDao
import com.yolbertdev.coffeeplatform.data.database.repository.LoanRepositoryImpl
import com.yolbertdev.coffeeplatform.data.database.repository.PaymentRepositoryImpl
import com.yolbertdev.coffeeplatform.data.database.repository.SyncRepositoryImpl
import com.yolbertdev.coffeeplatform.data.database.repository.UserRepositoryImpl
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import com.yolbertdev.coffeeplatform.domain.repository.SyncRepository
import com.yolbertdev.coffeeplatform.domain.repository.UserRepository
import com.yolbertdev.coffeeplatform.ui.login.LoginScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.detail.CustomerDetailScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.LoanScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.add.AddLoanScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.register.RegisterScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.edit.EditCustomerScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.payment.PaymentScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.payment.add.AddPaymentScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.sync.SyncScreenModel

val commonModule = module {

    single<CoffeeDatabase> { CoffeeDatabase(get()) }
    single<CustomerDao> { CustomerDao(get()) }
    single<LoanDao> { LoanDao(get()) }
    single<PaymentDao> { PaymentDao(get()) }
    
    single<HomeScreenModel> { HomeScreenModel(get()) }
    single<CustomerScreenModel>{CustomerScreenModel(get(), get())}
    
    single { InsertLoanUseCase(get()) }
    single { CreatePaymentUseCase(get()) }
    single { InsertCustomerUseCase(get()) }
    single { SelectAllCustomerUseCase(get()) }
    single { GetCustomerByIdUseCase(get()) }
    single { UpdateCustomerUseCase(get()) }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }

    single<UserRepository> { UserRepositoryImpl(get()) }

    // IMPORTANTE: Definir el repositorio como SINGLE para compartir el estado de los Flow
    single<CustomerRepository> { CustomerRepositoryImpl(get()) }

    singleOf(::LoanRepositoryImpl) bind LoanRepository::class
    singleOf(::PaymentRepositoryImpl) bind PaymentRepository::class

    single { CustomerDetailScreenModel(get(), get(), get()) }
    single { SyncDao(get()) }
    single<SyncRepository> { SyncRepositoryImpl(get()) }
    single { ExportBackupUseCase(get(), get()) }
    single { ImportBackupUseCase(get(), get()) }
    factory { SyncScreenModel(get(), get()) }
    factory<AddCustomerScreenModel>{ AddCustomerScreenModel(get(), get())}
    factory { AddLoanScreenModel(get(), get()) }
    factory { PaymentScreenModel(get()) }
    factory { AddPaymentScreenModel(get()) }
    factory { LoanScreenModel(get()) }
    factory { HomeScreenModel(get()) }
    factory { RegisterScreenModel(get()) }
    factory { LoginScreenModel(get()) }
    factory { EditCustomerScreenModel(get(), get()) }
    factory { ReportViewModel(get(),get(),get()) }
}