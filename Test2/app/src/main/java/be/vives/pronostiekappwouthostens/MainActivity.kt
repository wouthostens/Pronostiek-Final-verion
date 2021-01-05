package be.vives.pronostiekappwouthostens

import android.Manifest.permission.CAMERA
import android.Manifest.permission_group.CAMERA
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder.VideoSource.CAMERA
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import be.vives.pronostiekappwouthostens.databinding.ActivityMainBinding
import be.vives.pronostiekappwouthostens.registreren.RegistrerenFragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    var user : FirebaseUser?=null
    private val pickImage = 100

    private var fragment: RegistrerenFragment? = null
    fun afbeeldingKiezen(fragment: RegistrerenFragment)
    {
        this.fragment = fragment
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }
    private val cameraRequest = 1888
    fun camera(fragment: RegistrerenFragment)
    {
        this.fragment = fragment
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, cameraRequest)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            if (fragment != null) {
                fragment!!.setImage(data?.data)
            }
        }
        if (requestCode == cameraRequest) {
            if (fragment != null) {
                val uri = getImageUriFromBitmap(this.applicationContext, data!!.extras!!.get("data") as Bitmap)
                fragment!!.setImage(uri)
            }
        }
    }
    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }
    fun gotofragmentregistreren(v: View?) {
        setContentView(R.layout.registreren_fragment)
    }

    fun gotofragmentlogin(v: View?)
    {
        setContentView(R.layout.login_fragment)
    }
}