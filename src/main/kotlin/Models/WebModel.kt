package Models

import kotlinx.serialization.Serializable

@Serializable
data class WebModel(val name : String, val link : String, var read : Boolean = false)
