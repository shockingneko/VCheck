/*
 * Copyright 2018 Zihua Zeng (edvard_hua@live.com), Lang Feng (tearjeaker@hotmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.edvard.poseestimation.degree

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.media.ImageReader
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.support.v13.app.FragmentCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.Comparator
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import com.edvard.poseestimation.MainpageActivity
import com.edvard.poseestimation.R

/**
 * Basic fragments for the Camera.
 */
class Camera2BasicFragment : Fragment(), FragmentCompat.OnRequestPermissionsResultCallback {

  private var currentpose = "up"
  private var tmppose = "down"
  private var posebias = 0
  private var handtimerstart = false
  private var distancetimerstart = false
  private var teachtimerstart = false
  private var check_dis = false
  var progress_ciruclar: com.mikhaellopez.circularprogressbar.CircularProgressBar? = null
  private var keeptime: Long = 3000
  private var dis_bias = 0
  private var checkdir = 0

  private var white: ImageView? = null
  private var E_image: ImageView? = null
  private var digit: Int = 0
  private var image_name = "up"
  private var flag: Boolean = false
  private var score: Int = 0
  private var flagcount: Int = 0
  private var totalstop = false

  private var UserHeight : Float = 175.0F
  private var left_eye = 0
  private var ANS: TextView? = null

  private val lock = Any()
  private var runClassifier = false
  private var checkedPermissions = false
  private var textView: TextView? = null
  private var textureView: AutoFitTextureView? = null
  private var layoutFrame: AutoFitFrameLayout? = null
  private var drawView: DrawView? = null
  private var classifier: ImageClassifier? = null
  private var image: ImageView? = null

  /**
   * [TextureView.SurfaceTextureListener] handles several lifecycle events on a [ ].
   */
  private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

    override fun onSurfaceTextureAvailable(
            texture: SurfaceTexture,
            width: Int,
            height: Int
    ) {
      openCamera(width, height)
    }

    override fun onSurfaceTextureSizeChanged(
            texture: SurfaceTexture,
            width: Int,
            height: Int
    ) {
      configureTransform(width, height)
    }

    override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
      return true
    }

    override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {}
  }

  /**
   * ID of the current [CameraDevice].
   */
  private var cameraId: String? = null

  /**
   * A [CameraCaptureSession] for camera preview.
   */
  private var captureSession: CameraCaptureSession? = null

  /**
   * A reference to the opened [CameraDevice].
   */
  private var cameraDevice: CameraDevice? = null

  /**
   * The [android.util.Size] of camera preview.
   */
  private var previewSize: Size? = null

  /**
   * [CameraDevice.StateCallback] is called when [CameraDevice] changes its state.
   */
  private val stateCallback = object : CameraDevice.StateCallback() {

    override fun onOpened(currentCameraDevice: CameraDevice) {
      // This method is called when the camera is opened.  We start camera preview here.
      cameraOpenCloseLock.release()
      cameraDevice = currentCameraDevice
      createCameraPreviewSession()
    }

    override fun onDisconnected(currentCameraDevice: CameraDevice) {
      cameraOpenCloseLock.release()
      currentCameraDevice.close()
      cameraDevice = null
    }

    override fun onError(
            currentCameraDevice: CameraDevice,
            error: Int
    ) {
      cameraOpenCloseLock.release()
      currentCameraDevice.close()
      cameraDevice = null
      val activity = activity
      activity?.finish()
    }
  }

  /**
   * An additional thread for running tasks that shouldn't block the UI.
   */
  private var backgroundThread: HandlerThread? = null

  /**
   * A [Handler] for running tasks in the background.
   */
  private var backgroundHandler: Handler? = null

  /**
   * An [ImageReader] that handles image capture.
   */
  private var imageReader: ImageReader? = null

  /**
   * [CaptureRequest.Builder] for the camera preview
   */
  private var previewRequestBuilder: CaptureRequest.Builder? = null

  /**
   * [CaptureRequest] generated by [.previewRequestBuilder]
   */
  private var previewRequest: CaptureRequest? = null

  /**
   * A [Semaphore] to prevent the app from exiting before closing the camera.
   */
  private val cameraOpenCloseLock = Semaphore(1)

  /**
   * A [CameraCaptureSession.CaptureCallback] that handles events related to capture.
   */
  private fun judge(answer : Double){
    handtimer.cancel()
    distancetimer.cancel()
    stop_bar()
    if(!totalstop){
      totalstop = true
      val intent = Intent()
      intent.setClass(activity, MainpageActivity::class.java)
      intent.putExtra("fragment", "deg")
      intent.putExtra("eye", left_eye)
      intent.putExtra("score" , answer)
      startActivity(intent)
    }
  }

  private val captureCallback = object : CameraCaptureSession.CaptureCallback() {

    override fun onCaptureProgressed(
            session: CameraCaptureSession,
            request: CaptureRequest,
            partialResult: CaptureResult
    ) {
    }

    override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
    ) {
    }
  }

  private val requiredPermissions: Array<String>
    get() {
      val activity = activity
      return try {
        val info = activity
                .packageManager
                .getPackageInfo(activity.packageName, PackageManager.GET_PERMISSIONS)
        val ps = info.requestedPermissions
        if (ps != null && ps.isNotEmpty()) {
          ps
        } else {
          arrayOf()
        }
      } catch (e: Exception) {
        arrayOf()
      }

    }

  /**
   * Takes photos and classify them periodically.
   */
  private val periodicClassify = object : Runnable {
    override fun run() {
      synchronized(lock) {
        if (runClassifier) {
          classifyFrame()
        }
      }
      backgroundHandler!!.post(this)
    }
  }

  /**
   * Shows a [Toast] on the UI thread for the classification results.
   *
   * @param text The message to show
   */
  private fun showToast(text: String) {
    val activity = activity
    activity?.runOnUiThread {
      textView!!.text = text
      drawView!!.invalidate()
    }
  }

  private fun showpose(text: String) {
    val activity = activity
    activity?.runOnUiThread {
      ANS!!.text = text
      drawView!!.invalidate()
    }
  }

  private fun start_bar(){
    val activity = activity
    activity?.runOnUiThread {
      progress_ciruclar.apply {
        this?.visibility = View.VISIBLE
        this?.setProgressWithAnimation(100f, keeptime)
      }
    }
  }
  private fun stop_bar() {
    val activity = activity
    activity?.runOnUiThread {
      progress_ciruclar.apply {
        this?.visibility = View.INVISIBLE
        this?.setProgressWithAnimation(0f, 0)
      }
    }
  }

  private fun stop_white() {
    val activity = activity
    activity?.runOnUiThread {
      ANS?.visibility = View.VISIBLE
      E_image?.visibility = View.INVISIBLE
      white?.visibility = View.INVISIBLE
      image?.visibility = View.INVISIBLE
    }
  }

  private fun showposeimg(text: String) {
    val activity = activity
    activity?.runOnUiThread {
      if(text == "up")
        image!!.setImageResource(R.drawable.up)
      else if(text == "left")
        image!!.setImageResource(R.drawable.left)
      else if(text == "down")
        image!!.setImageResource(R.drawable.down)
      else if(text == "right")
        image!!.setImageResource(R.drawable.right)
      else
        image!!.setImageResource(R.drawable.refuse)
    }
  }

  /**
   * Layout the preview and buttons.
   */
  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_camera2_basic, container, false)
  }

  /**
   * Connect the buttons to their event handler.
   */
  override fun onViewCreated(
          view: View,
          savedInstanceState: Bundle?
  ) {

    ANS = view.findViewById(R.id.ANS)
    textureView = view.findViewById(R.id.texture)
    textView = view.findViewById(R.id.text)
    layoutFrame = view.findViewById(R.id.layout_frame)
    drawView = view.findViewById(R.id.drawview)
    image = view.findViewById(R.id.imageview)
    progress_ciruclar = view.findViewById(R.id.progress_circular)
    E_image = view.findViewById(R.id.E_image)
    white = view.findViewById(R.id.white)
  }

  /**
   * Load the model and labels.
   */
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    try {
      val activity = activity
      var height: SharedPreferences = activity.getSharedPreferences("template_data", Context.MODE_PRIVATE)
      UserHeight = height.getInt("height", 175).toFloat()
      left_eye = activity.intent.getIntExtra("left", 0)
      if(activity.intent.getBooleanExtra("jump", false)){
        checkdir = 4
      }

      classifier = ImageClassifierFloatInception.create(activity)
      if (drawView != null)
        drawView!!.setImgSize(classifier!!.imageSizeX, classifier!!.imageSizeY)
    } catch (e: IOException) {
      Log.e(TAG, "Failed to initialize an image classifier.", e)
    }
  }

  @Synchronized
  override fun onResume() {
    super.onResume()

    backgroundThread = HandlerThread(HANDLE_THREAD_NAME)
    backgroundThread!!.start()
    backgroundHandler = Handler(backgroundThread!!.getLooper())
    runClassifier = true

    startBackgroundThread(Runnable { classifier!!.initTflite(true) })
    startBackgroundThread(periodicClassify)

    // When the screen is turned off and turned back on, the SurfaceTexture is already
    // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
    // a camera and start preview from here (otherwise, we wait until the surface is ready in
    // the SurfaceTextureListener).
    if (textureView!!.isAvailable) {
      openCamera(textureView!!.width, textureView!!.height)
    } else {
      textureView!!.surfaceTextureListener = surfaceTextureListener
    }
  }

  override fun onPause() {
    closeCamera()
    stopBackgroundThread()
    super.onPause()
  }

  override fun onDestroy() {
    classifier!!.close()
    super.onDestroy()
  }

  /**
   * Sets up member variables related to camera.
   *
   * @param width  The width of available size for camera preview
   * @param height The height of available size for camera preview
   */
  private fun setUpCameraOutputs(
          width: Int,
          height: Int
  ) {
    val activity = activity
    val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      for (cameraId in manager.cameraIdList) {
        val characteristics = manager.getCameraCharacteristics(cameraId)

        // We don't use a front facing camera in this sample.
        val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
        if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
          continue
        }

        val map =
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: continue

        // // For still image captures, we use the largest available size.
        val largest = Collections.max(
                Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)), CompareSizesByArea()
        )
        imageReader = ImageReader.newInstance(
                largest.width, largest.height, ImageFormat.JPEG, /*maxImages*/ 2
        )

        // Find out if we need to swap dimension to get the preview size relative to sensor
        // coordinate.
        val displayRotation = activity.windowManager.defaultDisplay.rotation

        /* Orientation of the camera sensor */
        val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        var swappedDimensions = false
        when (displayRotation) {
          Surface.ROTATION_0, Surface.ROTATION_180 -> if (sensorOrientation == 90 || sensorOrientation == 270) {
            swappedDimensions = true
          }
          Surface.ROTATION_90, Surface.ROTATION_270 -> if (sensorOrientation == 0 || sensorOrientation == 180) {
            swappedDimensions = true
          }
          else -> Log.e(TAG, "Display rotation is invalid: $displayRotation")
        }

        val displaySize = Point()
        activity.windowManager.defaultDisplay.getSize(displaySize)
        var rotatedPreviewWidth = width
        var rotatedPreviewHeight = height
        var maxPreviewWidth = displaySize.x
        var maxPreviewHeight = displaySize.y

        if (swappedDimensions) {
          rotatedPreviewWidth = height
          rotatedPreviewHeight = width
          maxPreviewWidth = displaySize.y
          maxPreviewHeight = displaySize.x
        }

        if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
          maxPreviewWidth = MAX_PREVIEW_WIDTH
        }

        if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
          maxPreviewHeight = MAX_PREVIEW_HEIGHT
        }

        previewSize = chooseOptimalSize(
                map.getOutputSizes(SurfaceTexture::class.java),
                rotatedPreviewWidth,
                rotatedPreviewHeight,
                maxPreviewWidth,
                maxPreviewHeight,
                largest
        )

        // We fit the aspect ratio of TextureView to the size of preview we picked.
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
          layoutFrame!!.setAspectRatio(previewSize!!.width, previewSize!!.height)
          textureView!!.setAspectRatio(previewSize!!.width, previewSize!!.height)
          drawView!!.setAspectRatio(previewSize!!.width, previewSize!!.height)
        } else {
          layoutFrame!!.setAspectRatio(previewSize!!.height, previewSize!!.width)
          textureView!!.setAspectRatio(previewSize!!.height, previewSize!!.width)
          drawView!!.setAspectRatio(previewSize!!.height, previewSize!!.width)
        }

        this.cameraId = cameraId
        return
      }
    } catch (e: CameraAccessException) {
      Log.e(TAG, "Failed to access Camera", e)
    } catch (e: NullPointerException) {
      // Currently an NPE is thrown when the Camera2API is used but not supported on the
      // device this code runs.
      ErrorDialog.newInstance(getString(R.string.camera_error))
              .show(childFragmentManager, FRAGMENT_DIALOG)
    }

  }

  /**
   * Opens the camera specified by [Camera2BasicFragment.cameraId].
   */
  @SuppressLint("MissingPermission")
  private fun openCamera(
          width: Int,
          height: Int
  ) {
    if (!checkedPermissions && !allPermissionsGranted()) {
      FragmentCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE)
      return
    } else {
      checkedPermissions = true
    }
    setUpCameraOutputs(width, height)
    configureTransform(width, height)
    val activity = activity
    val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
        throw RuntimeException("Time out waiting to lock camera opening.")
      }
      manager.openCamera(cameraId!!, stateCallback, backgroundHandler)
    } catch (e: CameraAccessException) {
      Log.e(TAG, "Failed to open Camera", e)
    } catch (e: InterruptedException) {
      throw RuntimeException("Interrupted while trying to lock camera opening.", e)
    }

  }

  private fun allPermissionsGranted(): Boolean {
    for (permission in requiredPermissions) {
      if (ContextCompat.checkSelfPermission(
                      activity, permission
              ) != PackageManager.PERMISSION_GRANTED
      ) {
        return false
      }
    }
    return true
  }

  override fun onRequestPermissionsResult(
          requestCode: Int,
          permissions: Array<String>,
          grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  /**
   * Closes the current [CameraDevice].
   */
  private fun closeCamera() {
    try {
      cameraOpenCloseLock.acquire()
      if (null != captureSession) {
        captureSession!!.close()
        captureSession = null
      }
      if (null != cameraDevice) {
        cameraDevice!!.close()
        cameraDevice = null
      }
      if (null != imageReader) {
        imageReader!!.close()
        imageReader = null
      }
    } catch (e: InterruptedException) {
      throw RuntimeException("Interrupted while trying to lock camera closing.", e)
    } finally {
      cameraOpenCloseLock.release()
    }
  }

  /**
   * Starts a background thread and its [Handler].
   */
  @Synchronized
  protected fun startBackgroundThread(r: Runnable) {
    if (backgroundHandler != null) {
      backgroundHandler!!.post(r)
    }
  }

  /**
   * Stops the background thread and its [Handler].
   */
  private fun stopBackgroundThread() {
    backgroundThread!!.quitSafely()
    try {
      backgroundThread!!.join()
      backgroundThread = null
      backgroundHandler = null
      synchronized(lock) {
        runClassifier = false
      }
    } catch (e: InterruptedException) {
      Log.e(TAG, "Interrupted when stopping background thread", e)
    }

  }

  /**
   * Creates a new [CameraCaptureSession] for camera preview.
   */
  private fun createCameraPreviewSession() {
    try {
      val texture = textureView!!.surfaceTexture!!

      // We configure the size of default buffer to be the size of camera preview we want.
      texture.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)

      // This is the output Surface we need to start preview.
      val surface = Surface(texture)

      // We set up a CaptureRequest.Builder with the output Surface.
      previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
      previewRequestBuilder!!.addTarget(surface)

      // Here, we create a CameraCaptureSession for camera preview.
      cameraDevice!!.createCaptureSession(
              Arrays.asList(surface),
              object : CameraCaptureSession.StateCallback() {

                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                  // The camera is already closed
                  if (null == cameraDevice) {
                    return
                  }

                  // When the session is ready, we start displaying the preview.
                  captureSession = cameraCaptureSession
                  try {
                    // Auto focus should be continuous for camera preview.
                    previewRequestBuilder!!.set(
                            CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                    )

                    // Finally, we start displaying the camera preview.
                    previewRequest = previewRequestBuilder!!.build()
                    captureSession!!.setRepeatingRequest(
                            previewRequest!!, captureCallback, backgroundHandler
                    )
                  } catch (e: CameraAccessException) {
                    Log.e(TAG, "Failed to set up config to capture Camera", e)
                  }

                }

                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                  showToast("Failed")
                }
              }, null
      )
    } catch (e: CameraAccessException) {
      Log.e(TAG, "Failed to preview Camera", e)
    }

  }

  /**
   * Configures the necessary [android.graphics.Matrix] transformation to `textureView`. This
   * method should be called after the camera preview size is determined in setUpCameraOutputs and
   * also the size of `textureView` is fixed.
   *
   * @param viewWidth  The width of `textureView`
   * @param viewHeight The height of `textureView`
   */
  private fun configureTransform(
          viewWidth: Int,
          viewHeight: Int
  ) {
    val activity = activity
    if (null == textureView || null == previewSize || null == activity) {
      return
    }
    val rotation = activity.windowManager.defaultDisplay.rotation
    val matrix = Matrix()
    val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
    val bufferRect = RectF(0f, 0f, previewSize!!.height.toFloat(), previewSize!!.width.toFloat())
    val centerX = viewRect.centerX()
    val centerY = viewRect.centerY()
    if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
      bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
      matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
      val scale = Math.max(
              viewHeight.toFloat() / previewSize!!.height,
              viewWidth.toFloat() / previewSize!!.width
      )
      matrix.postScale(scale, scale, centerX, centerY)
      matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
    } else if (Surface.ROTATION_180 == rotation) {
      matrix.postRotate(180f, centerX, centerY)
    }
    textureView!!.setTransform(matrix)
  }

  /**
   * Classifies a frame from the preview stream.
   */
  private fun classifyFrame() {
    if (classifier == null || activity == null || cameraDevice == null) {
      showToast("Uninitialized Classifier or invalid context.")
      return
    }

    val bitmap = textureView!!.getBitmap(classifier!!.imageSizeX, classifier!!.imageSizeY)
    val textToShow = classifier!!.classifyFrame(bitmap)
    bitmap.recycle()


    drawView!!.setDrawPoint(classifier!!.mPrintPointArray!!, 0.5f)

    showToast(textToShow)


    val HF : Float = classifier!!.mPrintPointArray!![1][13] / 0.5f - classifier!!.mPrintPointArray!![1][0] / 0.5f
    if(HF > UserHeight*0.53) {
      if(distancetimerstart == true && dis_bias < 2)
        dis_bias ++
      else{
        showpose("請站遠一點")
        stop_bar()
        stop_white()
        distancetimer.cancel()
        handtimer.cancel()
        teachtimer.cancel()
        distancetimerstart = false
        check_dis = false
        dis_bias = 0
      }
    }
    else if (HF < UserHeight*0.43) {
      if(distancetimerstart == true && dis_bias < 2)
        dis_bias ++
      else{
        showpose("請站近一點")
        stop_bar()
        stop_white()
        distancetimer.cancel()
        handtimer.cancel()
        teachtimer.cancel()
        distancetimerstart = false
        check_dis = false
        dis_bias = 0
      }

    }
    else {
      if(classifier!!.mPrintPointArray!![0][0] / 0.5f < 70) {
        if(distancetimerstart == true && dis_bias < 2)
          dis_bias ++
        else{
          showpose("請往右一點")
          stop_bar()
          stop_white()
          distancetimer.cancel()
          handtimer.cancel()
          teachtimer.cancel()
          distancetimerstart = false
          check_dis = false
          dis_bias = 0
        }
      }
      else if(classifier!!.mPrintPointArray!![0][0] / 0.5f > 120) {
        if(distancetimerstart == true && dis_bias < 2)
          dis_bias ++
        else{
          showpose("請往左一點")
          stop_bar()
          stop_white()
          distancetimer.cancel()
          handtimer.cancel()
          teachtimer.cancel()
          distancetimerstart = false
          check_dis = false
          dis_bias = 0
        }
      }
      else{
        if(check_dis == false)
          showpose("Good")
        if(distancetimerstart != true && check_dis == false){
          start_bar()
          distancetimer.start()
          distancetimerstart = true
        }
      }
    }

    if(check_dis){
      var hand_x : Float = classifier!!.mPrintPointArray!![0][7]/  0.5f - classifier!!.mPrintPointArray!![0][6]/  0.5f
      var hand_y : Float = classifier!!.mPrintPointArray!![1][7]/  0.5f - classifier!!.mPrintPointArray!![1][6]/  0.5f


      if(left_eye == 1){
        hand_x = classifier!!.mPrintPointArray!![0][4]/  0.5f - classifier!!.mPrintPointArray!![0][3]/  0.5f
        hand_y = classifier!!.mPrintPointArray!![1][4]/  0.5f - classifier!!.mPrintPointArray!![1][3]/  0.5f
      }

      var compare_hand : Float = Math.abs(hand_x) / Math.abs(hand_y)

      if(hand_y < 0 && compare_hand < 1){
        tmppose = "up"
      }

      else if(hand_x < 0 && compare_hand > 1){
        tmppose = "left"
      }

      else if(hand_y > 0 && compare_hand < 1){
        tmppose = "down"
      }

      else if(hand_x > 0 && compare_hand > 1){
        tmppose = "right"
      }

      else{
        tmppose = "refuse"
      }

      if(checkdir > 3){
        showposeimg(tmppose)
        if(currentpose != tmppose){
          if(posebias > 1){
            posebias = 0
            currentpose = tmppose
            //showposeimg(currentpose)
            stop_bar()
            handtimer.cancel()
            start_bar()
            handtimer.start()
            handtimerstart = true
          }
          else{
            posebias ++
          }
        }
        else if(currentpose == tmppose){
          posebias = 0
          if(handtimerstart == false){
            start_bar()
            handtimer.start()
            handtimerstart = true
          }
        }
      }
      else{
        if(tmppose == image_name && !teachtimerstart){
          teachtimerstart = true
          start_bar()
          teachtimer.start()
        }
        else if(tmppose != image_name){
          teachtimerstart = false
          stop_bar()
          teachtimer.cancel()
        }
      }

    }
    else{
      showposeimg("refuse")
    }

  }

  val distancetimer = object : CountDownTimer(keeptime, 1000) {
    override fun onFinish() {
      check_dis = true
      distancetimerstart = false
      stop_bar()
      direction_check()
      val activity = activity
      activity?.runOnUiThread {
        E_image?.visibility = View.VISIBLE
        white?.visibility = View.VISIBLE
      }
    }

    override fun onTick(millisUntilFinished: Long) {
    }
  }

  val handtimer = object : CountDownTimer(keeptime, 1000) {
    override fun onFinish() {
      if(currentpose == image_name){
        if(flag == false)
          digit += 1
        else
          score += 1
      }
      else{
        flag = true
        score -= 1
      }

      if(flag == true){
        if(flagcount < 4)
          flagcount += 1
        else {
          if(score >= 1) {
            flag = false
            score = 0
            flagcount = 0
            digit += 1
          }
          else {
            judge(digit.toDouble() / 10)
          }
        }
        if(score >= 1) {
          flag = false
          score = 0
          flagcount = 0
          digit += 1
        }
      }
        Generate_image()

      stop_bar()
      handtimerstart = false
    }

    override fun onTick(millisUntilFinished: Long) {
    }
  }

  val teachtimer= object : CountDownTimer(keeptime, 1000){
    override fun onFinish() {
      checkdir ++
      direction_check()
      teachtimerstart = false
    }

    override fun onTick(millisUntilFinished: Long) {
    }
  }

  private fun direction_check(){
    when(checkdir){
      0 -> {
        image_name = "up"
        if(left_eye == 1) {
          showpose("請將左手向上舉")
          activity?.runOnUiThread {
            E_image!!.setImageResource(R.drawable.lefthand_up)
          }
        }
        else{
          showpose("請將右手向上舉")
          activity?.runOnUiThread {
            E_image!!.setImageResource(R.drawable.righthand_up)
          }
        }
      }
      1 -> {
        image_name = "right"
        if(left_eye == 1) {
          showpose("請將左手向右舉")
          activity?.runOnUiThread {
            E_image!!.setImageResource(R.drawable.lefthand_right)
          }
        }
        else{
          showpose("請將右手向右舉")
          activity?.runOnUiThread {
            E_image!!.setImageResource(R.drawable.righthand_right)
          }
        }
      }
      2 -> {
        image_name = "down"
        if(left_eye == 1) {
          showpose("請將左手向下舉")
          activity?.runOnUiThread {
            E_image!!.setImageResource(R.drawable.lefthand_down)
          }
        }
        else{
          showpose("請將右手向下舉")
          activity?.runOnUiThread {
            E_image!!.setImageResource(R.drawable.righthand_down)
          }
        }
      }
      3 -> {
        image_name = "left"
        if(left_eye == 1) {
          showpose("請將左手向左舉")
          activity?.runOnUiThread {
            E_image!!.setImageResource(R.drawable.lefthand_left)
          }
        }
        else{
          showpose("請將右手向左舉")
          activity?.runOnUiThread {
            E_image!!.setImageResource(R.drawable.righthand_left)
          }
        }
      }
      else -> {
        Generate_image()
        activity?.runOnUiThread {
          image?.visibility = View.VISIBLE
          ANS!!.visibility = View.INVISIBLE
        }
      }
    }
  }

  private fun getlen(digit: Int) : Int{
    val imagedp = intArrayOf(639, 321, 215, 162, 131, 109, 94, 83, 74, 67, 61, 56)
    return imagedp[digit]
  }

  private fun Generate_image(){
    var dice = (Math.random() * 4)
    val metrics = DisplayMetrics()
    val activity = activity
    activity.windowManager.defaultDisplay.getMetrics(metrics)
    //getWindowManager().getDefaultDisplay().getMetrics(metrics)

    if(image_name == "up" && dice >= 0 && dice < 1){
      dice += 1
    }
    else if(image_name == "down" && dice >= 1 && dice < 2){
      dice += 1
    }
    else if(image_name == "left" && dice >= 2 && dice < 3){
      dice += 1
    }
    else if(image_name == "right" && dice >= 3){
      dice -= 3
    }

    if(digit > 11){
      judge(1.2)
    }
    else{
      val changed_dp = getlen(digit) * (metrics.densityDpi / 420)
      val activity = activity
      if (dice >= 0 && dice < 1){
        image_name = "up"
        var bmp = BitmapFactory.decodeResource(resources, R.drawable.e_up)
        bmp = Bitmap.createScaledBitmap(bmp, changed_dp, changed_dp, true);

        activity?.runOnUiThread {
          E_image?.setImageBitmap(bmp)
        }
      }
      else if(dice >= 1 && dice < 2){
        image_name = "down"
        var bmp = BitmapFactory.decodeResource(resources, R.drawable.e_down)
        bmp = Bitmap.createScaledBitmap(bmp, changed_dp, changed_dp, true);

        activity?.runOnUiThread {
          E_image?.setImageBitmap(bmp)
        }
      }
      else if(dice >= 2 && dice < 3){
        image_name = "left"
        var bmp = BitmapFactory.decodeResource(resources, R.drawable.e_left)
        bmp = Bitmap.createScaledBitmap(bmp, changed_dp, changed_dp, true);

        activity?.runOnUiThread {
          E_image?.setImageBitmap(bmp)
        }
      }
      else{
        image_name = "right"
        var bmp = BitmapFactory.decodeResource(resources, R.drawable.e_right)
        bmp = Bitmap.createScaledBitmap(bmp, changed_dp, changed_dp, true);

        activity?.runOnUiThread {
          E_image?.setImageBitmap(bmp)
        }
      }
    }
  }


  /**
   * Compares two `Size`s based on their areas.
   */
  private class CompareSizesByArea : Comparator<Size> {

    override fun compare(
            lhs: Size,
            rhs: Size
    ): Int {
      // We cast here to ensure the multiplications won't overflow
      return java.lang.Long.signum(
              lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height
      )
    }
  }

  /**
   * Shows an error message dialog.
   */
  class ErrorDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
      val activity = activity
      return AlertDialog.Builder(activity)
              .setMessage(arguments.getString(ARG_MESSAGE))
              .setPositiveButton(
                      android.R.string.ok
              ) { dialogInterface, i -> activity.finish() }
              .create()
    }

    companion object {

      private val ARG_MESSAGE = "message"

      fun newInstance(message: String): ErrorDialog {
        val dialog = ErrorDialog()
        val args = Bundle()
        args.putString(ARG_MESSAGE, message)
        dialog.arguments = args
        return dialog
      }
    }
  }

  companion object {

    /**
     * Tag for the [Log].
     */
    private const val TAG = "TfLiteCameraDemo"

    private const val FRAGMENT_DIALOG = "dialog"

    private const val HANDLE_THREAD_NAME = "CameraBackground"

    private const val PERMISSIONS_REQUEST_CODE = 1

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private const val MAX_PREVIEW_WIDTH = 1920

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private const val MAX_PREVIEW_HEIGHT = 1080

    /**
     * Resizes image.
     *
     *
     * Attempting to use too large a preview size could  exceed the camera bus' bandwidth limitation,
     * resulting in gorgeous previews but the storage of garbage capture data.
     *
     *
     * Given `choices` of `Size`s supported by a camera, choose the smallest one that is
     * at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size, and
     * whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal `Size`, or an arbitrary one if none were big enough
     */
    private fun chooseOptimalSize(
            choices: Array<Size>,
            textureViewWidth: Int,
            textureViewHeight: Int,
            maxWidth: Int,
            maxHeight: Int,
            aspectRatio: Size
    ): Size {

      // Collect the supported resolutions that are at least as big as the preview Surface
      val bigEnough = ArrayList<Size>()
      // Collect the supported resolutions that are smaller than the preview Surface
      val notBigEnough = ArrayList<Size>()
      val w = aspectRatio.width
      val h = aspectRatio.height
      for (option in choices) {
        if (option.width <= maxWidth
                && option.height <= maxHeight
                && option.height == option.width * h / w
        ) {
          if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
            bigEnough.add(option)
          } else {
            notBigEnough.add(option)
          }
        }
      }

      // Pick the smallest of those big enough. If there is no one big enough, pick the
      // largest of those not big enough.
      return when {
        bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
        notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
        else -> {
          Log.e(TAG, "Couldn't find any suitable preview size")
          choices[0]
        }
      }
    }

    fun newInstance(): Camera2BasicFragment {
      return Camera2BasicFragment()
    }
  }
}
