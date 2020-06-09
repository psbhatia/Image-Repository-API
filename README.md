# Image Repository

RESTful API implementation of an image repository as a spring application.   



## API Documentation

### Login
```url
{environment}/api/v1/login
```
**Method**: POST     
**form-data**: email, password.    
**Authorization:** required  
**Return value**: jwt web token to access \api\v1\image endpoints.  

### Upload image
```url
{environment}/api/v1/images/upload
```
**Method**: POST     
**form-data**: file, description.   
**Authorization:** required  
**return value**: Confirmation if request to upload is valid.  
**Notes:**  Only accepts jpeg,jpg and png files

### Search for images
```url
{environment}/api/v1/images/search
```
**Method**: GET     
**form-data**: keyword   
**Authorization:** required  
**return value**: Object with information about found images  
**Notes:**  Will search the images based on description and name

### Delete an image from repository
```url
{environment}/api/v1/images/delete
```
**Method**: DELETE     
**form-data**: id   
**Authorization:** required  
**return value**: Confirmation of delete request  
**Notes:**  id can be retrieved from searching for image

### Download image from repository
```url
{environment}/api/v1/images/download
```
**Method**: GET     
**form-data**: id   
**Authorization:** required  
**return value**: File as byte array which can be saved directly from postman  



## Technologies used
The application itself is a java spring application. A postgresql database is used to query for images. These images are stored in an AWS S3 bucket. Authorization is done using JWT web tokens.