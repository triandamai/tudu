package app.trian.tudu.data.repository.design

import app.trian.tudu.domain.DataState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun checkIsUserLogin():Flow<Boolean>

    suspend fun loginBasic(email:String,password:String):Flow<DataState<FirebaseUser>>
    suspend fun registerBasic(email:String,password:String):Flow<DataState<FirebaseUser>>

    suspend fun loginGoogle():Flow<DataState<String>>

    suspend fun registerGoogle():Flow<DataState<String>>

    suspend fun signOut(callback:()->Unit)
}