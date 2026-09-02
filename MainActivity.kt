package com.alnasr.offline

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Product(val name:String,val price:String,val category:String,val description:String)

private val Black=Color(0xFF05070B)
private val Blue=Color(0xFF0D47A1)
private val Silver=Color(0xFFD9E2F0)

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState)
  val shared=intent?.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
  setContent{Theme{App(shared)}}
 }
}

@Composable fun Theme(content:@Composable()->Unit)=MaterialTheme(
 colorScheme=darkColorScheme(primary=Color(0xFF4EA1FF),background=Black,surface=Color(0xFF101722)),
 content=content
)

@Composable fun App(shared:String){
 var name by remember{mutableStateOf("")}
 var entered by remember{mutableStateOf(false)}
 var admin by remember{mutableStateOf(false)}
 var pass by remember{mutableStateOf("")}
 var login by remember{mutableStateOf(false)}
 var whatsapp by remember{mutableStateOf("")}
 var productName by remember{mutableStateOf("")}
 var productPrice by remember{mutableStateOf("")}
 var productCategory by remember{mutableStateOf("ملابس")}
 var productDesc by remember{mutableStateOf(shared)}
 var add by remember{mutableStateOf(false)}
 var products by remember{mutableStateOf(listOf<Product>())}

 if(!entered){ Welcome(name,{name=it}){if(name.isNotBlank())entered=true}; return }

 Scaffold(
  containerColor=Black,
  topBar={TopAppBar(
   title={Column{Text("النصر",fontWeight=FontWeight.ExtraBold);Text("البسة واكسسوارات النصر",fontSize=11.sp)}},
   actions={TextButton({login=true}){Text("الإدارة")}},
   colors=TopAppBarDefaults.topAppBarColors(containerColor=Black, titleContentColor=Color.White)
  )}
 ){pad->
  LazyColumn(Modifier.padding(pad).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
   item{Text("أهلًا بك، $name 💙",fontSize=22.sp,fontWeight=FontWeight.Bold)}
   item{Text("الدفع عند الاستلام • لا توجد خدمة توصيل",color=Color.LightGray)}
   item{
    if(products.isEmpty()) Card(colors=CardDefaults.cardColors(containerColor=Color(0xFF101722))){
     Column(Modifier.fillMaxWidth().padding(28.dp),horizontalAlignment=Alignment.CenterHorizontally){
      Text("لا توجد منتجات حاليًا",fontSize=20.sp,fontWeight=FontWeight.Bold)
      Text("ابدأ بإضافة أول منتجات النصر من لوحة الإدارة.",color=Silver)
     }
    }
   }
   items(products){p->
    Card(colors=CardDefaults.cardColors(containerColor=Color(0xFF101722))){
     Column(Modifier.padding(16.dp)){
      Text(p.name,fontSize=19.sp,fontWeight=FontWeight.Bold)
      Text(p.category,color=Color(0xFF4EA1FF))
      Text(p.description,color=Silver)
      Text("${p.price} • الدفع عند الاستلام",fontWeight=FontWeight.Bold)
     }
    }
   }
   item{
    Button(onClick={
     if(whatsapp.isNotBlank()){
      startActivity(Intent(Intent.ACTION_VIEW,android.net.Uri.parse("https://wa.me/"+whatsapp.filter{it.isDigit()})))
     }
    },modifier=Modifier.fillMaxWidth()){Text("تواصل معنا عبر واتساب")}
   }
  }
 }

 if(login) AlertDialog(
  onDismissRequest={login=false},
  title={Text("دخول الإدارة")},
  text={OutlinedTextField(pass,{pass=it},label={Text("كلمة المرور")},singleLine=true)},
  confirmButton={TextButton({if(pass=="2010"){admin=true;login=false;pass=""}}){Text("دخول")}},
  dismissButton={TextButton({login=false}){Text("إلغاء")}}
 )

 if(admin) AlertDialog(
  onDismissRequest={admin=false},
  title={Text("لوحة الإدارة")},
  text={Column(verticalArrangement=Arrangement.spacedBy(8.dp)){
   Text("إدارة محلية على هذا الهاتف.")
   OutlinedTextField(whatsapp,{whatsapp=it},label={Text("رقم واتساب الدولي")},singleLine=true)
   Button({add=true}){Text("إضافة منتج")}
  }},
  confirmButton={TextButton({admin=false}){Text("إغلاق")}}
 )

 if(add) AlertDialog(
  onDismissRequest={add=false},
  title={Text("إضافة منتج")},
  text={Column(verticalArrangement=Arrangement.spacedBy(7.dp)){
   OutlinedTextField(productName,{productName=it},label={Text("اسم المنتج")})
   OutlinedTextField(productPrice,{productPrice=it},label={Text("السعر")})
   OutlinedTextField(productCategory,{productCategory=it},label={Text("القسم")})
   OutlinedTextField(productDesc,{productDesc=it},label={Text("الوصف")})
  }},
  confirmButton={TextButton({
   if(productName.isNotBlank()){
    products=products+Product(productName,productPrice,productCategory,productDesc)
    productName="";productPrice="";productDesc="";add=false
   }
  }){Text("نشر")}},
  dismissButton={TextButton({add=false}){Text("إلغاء")}}
 )
}

@Composable fun Welcome(name:String,onName:(String)->Unit,onGo:()->Unit){
 Box(Modifier.fillMaxSize().background(Black).padding(28.dp),contentAlignment=Alignment.Center){
  Column(horizontalAlignment=Alignment.CenterHorizontally){
   Text("النصر",fontSize=48.sp,fontWeight=FontWeight.ExtraBold,color=Color(0xFF4EA1FF))
   Text("البسة واكسسوارات النصر",fontSize=22.sp,fontWeight=FontWeight.Bold)
   Text("أناقتك تبدأ من النصر",color=Silver)
   Spacer(Modifier.height(30.dp))
   OutlinedTextField(name,onName,label={Text("اكتب اسمك")},singleLine=true)
   Spacer(Modifier.height(14.dp))
   Button(onGo,enabled=name.isNotBlank()){Text("دخول المتجر")}
  }
 }
}
