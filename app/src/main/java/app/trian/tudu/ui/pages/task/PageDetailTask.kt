 package app.trian.tudu.ui.pages.task

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.trian.tudu.R
import app.trian.tudu.common.Routes
import app.trian.tudu.common.toReadableDate
import app.trian.tudu.data.local.Category
import app.trian.tudu.data.local.Task
import app.trian.tudu.data.local.Todo
import app.trian.tudu.ui.component.ItemAddTodo
import app.trian.tudu.ui.component.ItemTodo
import app.trian.tudu.ui.component.dialog.DropdownPickCategory
import app.trian.tudu.ui.theme.Inactivebackground
import app.trian.tudu.ui.theme.TuduTheme
import app.trian.tudu.viewmodel.TaskViewModel
import compose.icons.Octicons
import compose.icons.octicons.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.DateTime

 /**
 * Detail task
 * author Trian Damai
 * created_at 29/01/22 - 17.28
 * site https://trian.app
 */
@Composable
fun PageDetailTask(
    modifier: Modifier = Modifier,
    router: NavHostController
) {
     val ctx = LocalContext.current
     val scope = rememberCoroutineScope()
     val currentBackStack = router.currentBackStackEntryAsState()
     val taskViewModel = hiltViewModel<TaskViewModel>()

     val detailTask by taskViewModel.detailTask.observeAsState(initial = Task())
     val completeTodo by taskViewModel.completeTodo.observeAsState(initial = emptyList())
     val unCompleteTodo by taskViewModel.unCompleteTodo.observeAsState(initial = emptyList())
     val listCategory by taskViewModel.listCategory.observeAsState(initial = emptyList())
     var date by remember {
         mutableStateOf(DateTime())
     }
     var taskId by remember {
         mutableStateOf("")
     }
     var taskName by remember {
         mutableStateOf(TextFieldValue(text = detailTask.name))
     }
     var deadlineState by remember {
         mutableStateOf(detailTask.deadline)
     }
     var reminderState by remember {
         mutableStateOf(detailTask.reminder)
     }
     var taskCategoryId by remember {
         mutableStateOf(detailTask.category_id)
     }
     var selectedCategory by remember {
         mutableStateOf(Category())
     }

     var showDropdownCategory by remember {
         mutableStateOf(false)
     }


     fun addPlainTodo() {
         taskViewModel.addNewTodo(
             "",
             taskId
         )
     }

     fun updateTodo(todo: Todo) {
         taskViewModel.updateTodo(todo)
     }

     fun deleteTodo(todo: Todo) {
         taskViewModel.deleteTodo(todo)
     }

     fun updateTask() {
         scope.launch {
             delay(800)
             detailTask.apply {
                 name = taskName.text
                 deadline = deadlineState
                 done = reminderState
                 category_id = taskCategoryId

             }

             taskViewModel.updateTask(detailTask)
         }
     }

     fun getMonth(month: Int): Int {
         if (month < 1) {
             return month
         }
         return month - 1
     }

     val datePickerDialog =
         DatePickerDialog(ctx, { _: DatePicker, year: Int, month: Int, day: Int ->
             date = DateTime(year, (month + 1), day, 0, 0)
             deadlineState = date.millis

             updateTask()

         }, date.year, getMonth(date.monthOfYear), date.dayOfMonth)


     LaunchedEffect(key1 = Unit, block = {
         taskId = currentBackStack.value?.arguments?.getString("taskId") ?: ""
         taskViewModel.getTaskById(taskId)
         taskViewModel.getCompleteTodo(taskId)
         taskViewModel.getUnCompleteTodo(taskId)

         delay(100)
         taskName = TextFieldValue(text = detailTask.name)
         deadlineState = detailTask.deadline
         reminderState = detailTask.reminder
         date = DateTime(
             detailTask.deadline
         )
         taskCategoryId = detailTask.category_id
         selectedCategory = listCategory.firstOrNull { it.categoryId == detailTask.category_id }
                 ?: Category(name = ctx.getString(R.string.no_category))


     })

     Scaffold(
         topBar = {
             TopAppBar(
                 backgroundColor = MaterialTheme.colors.background,
                 elevation = 0.dp,
                 navigationIcon = {
                     IconToggleButton(
                         checked = false, onCheckedChange = {
                             router.popBackStack()
                         }
                     ) {
                         Icon(
                             imageVector = Octicons.ArrowLeft16,
                             contentDescription = ""
                         )
                     }
                 },
                 title = {

                 }
             )
         }
     ) {

         Column(
             modifier = modifier
                 .fillMaxSize()
                 .background(MaterialTheme.colors.background),
             horizontalAlignment = Alignment.Start,
             verticalArrangement = Arrangement.Top
         ) {
             Box(modifier = modifier.padding(horizontal = 20.dp)) {
                 Row(
                     modifier = modifier
                         .clip(
                             RoundedCornerShape(
                                 topStart = 10.dp,
                                 topEnd = 10.dp,
                                 bottomStart = 10.dp,
                                 bottomEnd = 10.dp
                             )
                         )
                         .background(MaterialTheme.colors.primary)
                         .padding(
                             vertical = 10.dp,
                             horizontal = 16.dp
                         )
                         .clickable {
                             showDropdownCategory = true
                         }
                 ) {
                     Text(
                         text = selectedCategory.name,
                         style = TextStyle(
                             color = MaterialTheme.colors.onPrimary
                         )
                     )
                     Icon(
                         imageVector = Octicons.ChevronDown16,
                         contentDescription = ""
                     )

                     DropdownPickCategory(
                         show = showDropdownCategory,
                         listCategory = listCategory,
                         onPick = {
                             taskCategoryId = it.categoryId
                             selectedCategory = it
                             updateTask()
                         },
                         buttonAddCategory = {

                         },
                         onHide = {
                             showDropdownCategory = false
                         }
                     )
                 }
             }
             LazyColumn(content = {
                 item {

                     Spacer(modifier = modifier.height(16.dp))
                     TextField(
                         modifier = modifier.fillMaxWidth(),
                         value = taskName,
                         onValueChange = {
                             taskName = it
                             updateTask()
                         },
                         placeholder = {
                             Text(
                                 text = taskName.text.ifBlank { stringResource(R.string.placeholder_input_task) },
                                 style = TextStyle(
                                     fontSize = 36.sp,
                                     fontWeight = FontWeight.Bold,
                                     color = MaterialTheme.colors.onBackground
                                 )
                             )
                         },
                         textStyle = TextStyle(
                             fontSize = 36.sp,
                             fontWeight = FontWeight.Bold,
                             color = MaterialTheme.colors.onBackground
                         ),
                         colors = TextFieldDefaults.textFieldColors(
                             focusedIndicatorColor = Color.Transparent,
                             unfocusedIndicatorColor = Color.Transparent,
                             textColor = MaterialTheme.colors.onSurface,
                             backgroundColor = Color.Transparent,
                         )
                     )
                 }
                 item {
                     Box(
                         modifier = modifier.padding(
                             horizontal = 20.dp
                         )
                     ) {
                         Spacer(modifier = modifier.height(10.dp))
                         Text(
                             text = stringResource(R.string.label_incomplete_todo),
                             style = TextStyle(
                                 fontSize = 20.sp,
                                 fontWeight = FontWeight.SemiBold,
                                 color = MaterialTheme.colors.onBackground
                             )
                         )
                     }
                 }
                 items(unCompleteTodo) { data ->
                     Box(
                         modifier = modifier.padding(
                             horizontal = 20.dp
                         )
                     ) {
                         ItemTodo(
                             todo = data,
                             onDone = {
                                 updateTodo(it)
                             },
                             onChange = {
                                 updateTodo(it)
                             },
                             onDelete = {
                                 deleteTodo(it)
                             }
                         )
                     }
                 }
                 item {
                     Box(
                         modifier = modifier.padding(
                             horizontal = 20.dp
                         )
                     ) {
                         ItemAddTodo {
                             addPlainTodo()
                         }
                     }
                 }
                 item {
                     Box(
                         modifier = modifier.padding(
                             horizontal = 20.dp
                         )
                     ) {
                         Spacer(modifier = modifier.height(10.dp))
                         Text(
                             text = stringResource(R.string.label_completed_todo),
                             style = TextStyle(
                                 fontSize = 20.sp,
                                 fontWeight = FontWeight.SemiBold,
                                 color = MaterialTheme.colors.onBackground
                             )
                         )
                         Spacer(modifier = modifier.height(10.dp))
                     }
                 }
                 items(completeTodo) { data ->
                     Box(
                         modifier = modifier.padding(
                             horizontal = 20.dp
                         )
                     ) {
                         ItemTodo(
                             todo = data,
                             onDone = {
                                 updateTodo(it)
                             },
                             onChange = {
                                 updateTodo(it)
                             },
                             onDelete = {
                                 deleteTodo(it)
                             }
                         )
                     }
                 }
                 item {
                     Spacer(modifier = modifier.height(16.dp))
                     Divider(
                         color = Inactivebackground,
                         thickness = 1.dp
                     )
                     Spacer(modifier = modifier.height(16.dp))
                     Box(
                         modifier = modifier.padding(
                             horizontal = 20.dp
                         )
                     ) {
                         Row(
                             modifier = modifier.fillMaxWidth(),
                             horizontalArrangement = Arrangement.SpaceBetween,
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             Row {
                                 Icon(
                                     imageVector = Octicons.Calendar16,
                                     contentDescription = "",
                                     tint = MaterialTheme.colors.onBackground
                                 )
                                 Spacer(modifier = modifier.width(6.dp))
                                 Text(
                                     text = stringResource(R.string.label_due_date),
                                     color = MaterialTheme.colors.onBackground
                                 )
                             }
                             Row(
                                 modifier = modifier
                                     .clip(
                                         RoundedCornerShape(
                                             topStart = 10.dp,
                                             topEnd = 10.dp,
                                             bottomStart = 10.dp,
                                             bottomEnd = 10.dp
                                         )
                                     )
                                     .background(Inactivebackground)
                                     .padding(
                                         horizontal = 10.dp,
                                         vertical = 2.dp
                                     )
                                     .clickable {
                                         datePickerDialog.show()
                                     }
                             ) {
                                 Text(
                                     text = deadlineState.toReadableDate(),
                                     style = TextStyle(
                                         color = MaterialTheme.colors.onPrimary
                                     )
                                 )
                             }
                         }
                     }
                     Spacer(modifier = modifier.height(16.dp))
                 }
                 item {
                     Divider(
                         color = Inactivebackground,
                         thickness = 0.6.dp
                     )
                     Spacer(modifier = modifier.height(16.dp))
                     Box(
                         modifier = modifier.padding(
                             horizontal = 20.dp
                         )
                     ) {
                         Row(
                             modifier = modifier.fillMaxWidth(),
                             horizontalArrangement = Arrangement.SpaceBetween,
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             Row {
                                 Icon(
                                     imageVector = Octicons.Clock16,
                                     contentDescription = "",
                                     tint = MaterialTheme.colors.onBackground
                                 )
                                 Spacer(modifier = modifier.width(6.dp))
                                 Text(
                                     text = stringResource(R.string.label_reminder),
                                     style = TextStyle(
                                         color = MaterialTheme.colors.onBackground
                                     )
                                 )
                             }
                             Row(
                                 modifier = modifier
                                     .clip(
                                         RoundedCornerShape(
                                             topStart = 10.dp,
                                             topEnd = 10.dp,
                                             bottomStart = 10.dp,
                                             bottomEnd = 10.dp
                                         )
                                     )
                                     .background(Inactivebackground)
                                     .padding(
                                         horizontal = 10.dp,
                                         vertical = 2.dp
                                     )
                                     .clickable {
                                         reminderState = !reminderState
                                         updateTask()
                                     }
                             ) {
                                 Text(
                                     text = if (reminderState) stringResource(R.string.reminder_yes)
                                     else stringResource(R.string.reminder_no),
                                     style = TextStyle(
                                         color = MaterialTheme.colors.onPrimary
                                     )
                                 )
                             }
                         }
                     }
                     Spacer(modifier = modifier.height(16.dp))
                 }
                 item {
                     Divider(
                         color = Inactivebackground,
                         thickness = 0.6.dp
                     )
                     Box(
                         modifier = modifier
                             .clickable {

                                 router.navigate("${Routes.ADD_NOTE}/${detailTask.taskId}")
                             }
                             .padding(
                                 horizontal = 20.dp,
                                 vertical = 16.dp
                             )
                     ) {
                         Row(
                             modifier = modifier.fillMaxWidth(),
                             horizontalArrangement = Arrangement.SpaceBetween,
                             verticalAlignment = Alignment.Top
                         ) {
                             Row(
                                 horizontalArrangement = Arrangement.Start,
                                 verticalAlignment = Alignment.Top
                             ) {
                                 Icon(
                                     imageVector = Octicons.Note16,
                                     contentDescription = "",
                                     tint = MaterialTheme.colors.onBackground
                                 )
                                 Spacer(modifier = modifier.width(6.dp))
                                 Column(
                                     modifier = modifier.fillMaxWidth(fraction = 0.8f)
                                 ) {
                                     Text(
                                         text = stringResource(R.string.label_note),
                                         style = TextStyle(
                                             color = MaterialTheme.colors.onBackground
                                         )
                                     )
                                     Text(
                                         text = detailTask.note,
                                         style = TextStyle(
                                             color = MaterialTheme.colors.onBackground
                                         )
                                     )
                                 }
                             }
                             Row(
                                 horizontalArrangement = Arrangement.End,
                                 verticalAlignment = Alignment.Top,
                                 modifier = modifier
                                     .clip(
                                         RoundedCornerShape(
                                             topStart = 10.dp,
                                             topEnd = 10.dp,
                                             bottomStart = 10.dp,
                                             bottomEnd = 10.dp
                                         )
                                     )
                                     .background(Inactivebackground)
                                     .padding(
                                         horizontal = 10.dp,
                                         vertical = 2.dp
                                     )
                             ) {
                                 Text(
                                     text = stringResource(R.string.btn_add_note),
                                     style = TextStyle(
                                         color = MaterialTheme.colors.onPrimary
                                     )
                                 )
                             }
                         }
                     }

                 }
             })
         }


     }
 }



@Preview
@Composable
fun PreviewPageDetailTask(){
    TuduTheme {
        PageDetailTask(router = rememberNavController())
    }
}