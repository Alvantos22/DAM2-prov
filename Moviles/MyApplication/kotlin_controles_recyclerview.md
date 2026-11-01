# Kotlin Android — XML + RecyclerView + controles

> **Objetivo:** saber qué ID crear en el XML, cómo recogerlo en Kotlin, qué propiedad o listener usar y en qué archivo va cada cosa.

## 1. Dónde va cada cosa

| Archivo | Qué se escribe aquí |
|---|---|
| `activity_main.xml` | Controles de la pantalla principal: `EditText`, botones, `RecyclerView`, `SeekBar`, etc. |
| `item_usuario.xml` | Controles que se repiten en cada fila del RecyclerView. |
| `Usuario.kt` | Datos de cada elemento: nombre, marcado, progreso, etc. |
| `UsuarioAdapter.kt` | Mostrar cada elemento y responder a clics/cambios de los controles de cada fila. |
| `UsuarioViewModel.kt` | Lista y lógica/datos si el ejercicio usa MVVM. |
| `MainActivity.kt` | Buscar vistas por ID, configurar RecyclerView, observar datos y conectar botones/controles de la pantalla. |

**Regla rápida:** si el control está en la pantalla una sola vez, normalmente va en `activity_main.xml` y se recoge en `MainActivity.kt`. Si aparece en cada fila, va en `item_usuario.xml` y se controla desde `UsuarioAdapter.kt`.

---

## 2. Plantilla base para conectar controles

### XML

```xml
<EditText
    android:id="@+id/etNombre"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Nombre" />

<Button
    android:id="@+id/btnAnadir"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Añadir" />

<CheckBox
    android:id="@+id/cbActivo"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Activo" />
```

### Kotlin (`MainActivity.kt`, dentro de `onCreate()` y después de `setContentView(...)`)

```kotlin
val etNombre = findViewById<EditText>(R.id.etNombre)
val btnAnadir = findViewById<Button>(R.id.btnAnadir)
val cbActivo = findViewById<CheckBox>(R.id.cbActivo)
```

Imports típicos:

```kotlin
import android.widget.EditText
import android.widget.Button
import android.widget.CheckBox
```

---

## 3. `CheckBox`: saber si está marcado

### XML: ID

```xml
<CheckBox
    android:id="@+id/cbSeleccionado"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Seleccionado" />
```

### Kotlin: leer el estado en cualquier función que tenga acceso a la vista

```kotlin
if (cbSeleccionado.isChecked) {
    // Está marcado
} else {
    // No está marcado
}
```

Para guardar el valor en una variable:

```kotlin
val marcado = cbSeleccionado.isChecked
```

Para marcar/desmarcar desde código:

```kotlin
cbSeleccionado.isChecked = true
cbSeleccionado.isChecked = false
```

### Detectar cuando cambia

**Dónde:** `MainActivity.kt`, dentro de `onCreate()`, después de `findViewById`.

```kotlin
cbSeleccionado.setOnCheckedChangeListener { _, marcado ->
    if (marcado) {
        // Se ha marcado
    } else {
        // Se ha desmarcado
    }
}
```

- `marcado` es un `Boolean`.
- `isChecked` **lee** el estado actual.
- `setOnCheckedChangeListener` detecta el cambio.

### Si el CheckBox está dentro de una fila del RecyclerView

**XML (`item_usuario.xml`):**

```xml
<CheckBox
    android:id="@+id/cbSeleccionado"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

**Modelo (`Usuario.kt`):**

```kotlin
data class Usuario(
    val nombre: String,
    var seleccionado: Boolean = false
)
```

**Adapter (`onBindViewHolder`)**:

```kotlin
holder.cbSeleccionado.setOnCheckedChangeListener(null)
holder.cbSeleccionado.isChecked = usuario.seleccionado

holder.cbSeleccionado.setOnCheckedChangeListener { _, marcado ->
    onSeleccionar(usuario, marcado)
}
```

El callback `onSeleccionar` debe actualizar el elemento en el ViewModel o en la lista. Se quita primero el listener para evitar que asignar `isChecked` al reciclar una fila dispare cambios involuntarios.

---

## 4. `Switch` / `SwitchCompat`: interruptor sí/no

### XML

```xml
<androidx.appcompat.widget.SwitchCompat
    android:id="@+id/swActivo"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Activo" />
```

### Kotlin

```kotlin
val swActivo = findViewById<SwitchCompat>(R.id.swActivo)

val activo = swActivo.isChecked
swActivo.isChecked = true

swActivo.setOnCheckedChangeListener { _, activado ->
    // activado es Boolean
}
```

Import:

```kotlin
import androidx.appcompat.widget.SwitchCompat
```

**Úsalo igual que un CheckBox** para consultar y detectar el estado.

---

## 5. `SeekBar`: deslizador numérico

### XML

```xml
<SeekBar
    android:id="@+id/sbVolumen"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:max="100"
    android:progress="25" />
```

### Kotlin (`MainActivity.kt`, dentro de `onCreate()`)

```kotlin
val sbVolumen = findViewById<SeekBar>(R.id.sbVolumen)

// Leer valor actual
val valor = sbVolumen.progress

// Cambiarlo desde código
sbVolumen.progress = 50

// Detectar cambios
sbVolumen.setOnSeekBarChangeListener(
    object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(
            seekBar: SeekBar?,
            progress: Int,
            fromUser: Boolean
        ) {
            // progress = valor actual
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // El usuario empieza a arrastrar
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // El usuario deja de arrastrar
        }
    }
)
```

Import:

```kotlin
import android.widget.SeekBar
```

- `max`: valor máximo.
- `progress`: valor actual.
- `progress` es un `Int`.
- Para mostrar el valor, actualiza un `TextView` dentro de `onProgressChanged`.

Ejemplo:

```kotlin
tvValor.text = progress.toString()
```

### Si el enunciado dice “slider”

En Android XML clásico normalmente se usa `SeekBar`. No lo confundas con `Slider` de Material, que es otro componente.

---

## 6. `RatingBar`: valoración por estrellas

### XML

```xml
<RatingBar
    android:id="@+id/rbNota"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:numStars="5"
    android:stepSize="1"
    android:rating="3" />
```

### Kotlin

```kotlin
val rbNota = findViewById<RatingBar>(R.id.rbNota)

val nota = rbNota.rating // Float
rbNota.rating = 4f

rbNota.setOnRatingBarChangeListener { _, rating, fromUser ->
    // rating es Float
}
```

Import:

```kotlin
import android.widget.RatingBar
```

- `numStars`: cantidad de estrellas.
- `rating`: valor actual, tipo `Float`.
- Usa `4f`, no `4`, al asignar una valoración.

---

## 7. `Spinner`: desplegable de opciones

### XML

```xml
<Spinner
    android:id="@+id/spCategoria"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

### Kotlin: configurar opciones y detectar selección

```kotlin
val spCategoria = findViewById<Spinner>(R.id.spCategoria)

val opciones = listOf("Java", "Kotlin", "Python")

val spinnerAdapter = ArrayAdapter(
    this,
    android.R.layout.simple_spinner_item,
    opciones
)

spinnerAdapter.setDropDownViewResource(
    android.R.layout.simple_spinner_dropdown_item
)

spCategoria.adapter = spinnerAdapter

spCategoria.onItemSelectedListener =
    object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            val opcion = opciones[position]
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
```

Imports:

```kotlin
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
```

- `position`: índice de la opción elegida.
- `opciones[position]`: texto elegido.
- El listener puede ejecutarse al configurar el Spinner, no solo cuando el usuario lo toca.

---

## 8. `RadioGroup` y `RadioButton`: elegir una sola opción

### XML

```xml
<RadioGroup
    android:id="@+id/rgTurno"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <RadioButton
        android:id="@+id/rbManana"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Mañana" />

    <RadioButton
        android:id="@+id/rbTarde"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Tarde" />
</RadioGroup>
```

### Kotlin

```kotlin
val rgTurno = findViewById<RadioGroup>(R.id.rgTurno)

rgTurno.setOnCheckedChangeListener { group, checkedId ->
    when (checkedId) {
        R.id.rbManana -> {
            // Eligió mañana
        }
        R.id.rbTarde -> {
            // Eligió tarde
        }
        -1 -> {
            // Ninguno seleccionado, si se permite
        }
    }
}
```

Para consultar qué ID está marcado:

```kotlin
val idMarcado = rgTurno.checkedRadioButtonId
```

Para saber si uno concreto está marcado:

```kotlin
val mananaMarcana = findViewById<RadioButton>(
    R.id.rbManana
).isChecked
```

---

## 9. `EditText`: leer, comprobar y limpiar texto

### XML

```xml
<EditText
    android:id="@+id/etNombre"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Nombre" />
```

### Kotlin

```kotlin
val texto = etNombre.text.toString().trim()

if (texto.isNotEmpty()) {
    // Hay texto
}

etNombre.setText("Santos") // Poner texto
etNombre.text.clear()      // Vaciar campo
```

Para números enteros:

```kotlin
val numero = etEdad.text.toString().toIntOrNull()

if (numero != null) {
    // Número válido
}
```

Para decimales:

```kotlin
val precio = etPrecio.text.toString().toDoubleOrNull()
```

**Consejo:** usa `toIntOrNull()` en vez de `toInt()` si el usuario puede dejar el campo vacío o escribir algo no numérico.

---

## 10. `Button`: clic y acciones

### XML

```xml
<Button
    android:id="@+id/btnAnadir"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Añadir" />
```

### Kotlin

```kotlin
val btnAnadir = findViewById<Button>(R.id.btnAnadir)

btnAnadir.setOnClickListener {
    // Código que se ejecuta al pulsar
}
```

Deshabilitar/habilitar:

```kotlin
btnAnadir.isEnabled = false
btnAnadir.isEnabled = true
```

Mostrar u ocultar:

```kotlin
btnAnadir.visibility = View.VISIBLE
btnAnadir.visibility = View.GONE
```

Importa `android.view.View` si usas `View.VISIBLE` o `View.GONE`.

---

## 11. `TextView`: mostrar resultados

### XML

```xml
<TextView
    android:id="@+id/tvResultado"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Resultado" />
```

### Kotlin

```kotlin
tvResultado.text = "Hola"
tvResultado.text = cantidad.toString()
```

Si quieres mostrar un número en un `TextView`, conviértelo a texto con `.toString()`.

---

## 12. `RecyclerView`: añadir, borrar y actualizar

### XML de la pantalla principal

```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/rvUsuarios"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_weight="1" />
```

### Kotlin: configurar en `MainActivity.onCreate()`

```kotlin
val rvUsuarios = findViewById<RecyclerView>(R.id.rvUsuarios)

rvUsuarios.layoutManager = LinearLayoutManager(this)
rvUsuarios.adapter = adapter
```

Imports:

```kotlin
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
```

### Si tienes una lista mutable directamente en el Adapter

```kotlin
private val usuarios = mutableListOf<Usuario>()
```

Añadir:

```kotlin
usuarios.add(nuevoUsuario)
notifyItemInserted(usuarios.lastIndex)
```

Borrar por posición:

```kotlin
usuarios.removeAt(position)
notifyItemRemoved(position)
```

Actualizar toda la lista:

```kotlin
usuarios.clear()
usuarios.addAll(nuevaLista)
notifyDataSetChanged()
```

**Importante:** el ejemplo anterior de lista mutable es una alternativa sencilla. Si el ejercicio usa MVVM y `LiveData`, cambia los datos en el ViewModel y observa la lista desde la Activity. No mantengas dos listas independientes que se puedan desincronizar.

---

## 13. Controles dentro de una fila del RecyclerView

Si cada fila tiene su propio `CheckBox`, botón o `SeekBar`:

1. Define el control en `item_usuario.xml`.
2. Declara su referencia en `UsuarioViewHolder`.
3. Configura su estado y listener en `onBindViewHolder()`.
4. Si el cambio modifica datos, comunícalo al ViewModel o a una función callback.

Ejemplo de `ViewHolder`:

```kotlin
class UsuarioViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    val tvNombre: TextView =
        view.findViewById(R.id.tvNombre)

    val cbSeleccionado: CheckBox =
        view.findViewById(R.id.cbSeleccionado)

    val btnBorrar: Button =
        view.findViewById(R.id.btnBorrar)
}
```

Dentro de `onBindViewHolder()`:

```kotlin
val usuario = usuarios[position]

holder.tvNombre.text = usuario.nombre

holder.cbSeleccionado.setOnCheckedChangeListener(null)
holder.cbSeleccionado.isChecked = usuario.seleccionado
holder.cbSeleccionado.setOnCheckedChangeListener { _, marcado ->
    onSeleccionar(usuario, marcado)
}

holder.btnBorrar.setOnClickListener {
    onBorrar(usuario)
}
```

Los nombres `onSeleccionar` y `onBorrar` representan callbacks que debes declarar en el constructor del Adapter.

**No pongas `findViewById(R.id.cbSeleccionado)` directamente en la Activity para controlar todos los checkbox de las filas:** cada fila tiene su propia instancia.

---

## 14. Modelo: qué propiedad usar según el control

| Control o dato | Propiedad típica del modelo | Tipo |
|---|---|---|
| Checkbox | `seleccionado` | `Boolean` |
| Switch | `activo` | `Boolean` |
| SeekBar | `progreso` | `Int` |
| RatingBar | `nota` | `Float` |
| Spinner | `categoria` | `String` |
| Texto | `nombre` | `String` |
| Edad | `edad` | `Int` |
| Precio | `precio` | `Double` |

Ejemplo:

```kotlin
data class Tarea(
    val nombre: String,
    var completada: Boolean = false,
    var progreso: Int = 0,
    var prioridad: Float = 0f
)
```

Los tipos dependen de lo que pida el ejercicio; no necesitas guardar en el modelo un dato que nunca se utilice.

---

## 15. Imports que suelen faltar

```kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
```

No pegues todos los imports sin mirar: utiliza los que correspondan a tus controles. Android Studio normalmente puede importarlos con `Alt + Enter`.

---

## 16. Tabla definitiva: qué escribir según el enunciado

| Si el enunciado dice… | XML | Kotlin |
|---|---|---|
| “Saber si está marcado” | `CheckBox` con ID | `cb.isChecked` |
| “Cuando se marque/desmarque” | `CheckBox` con ID | `setOnCheckedChangeListener` |
| “Cambiar valor con deslizador” | `SeekBar` con `max` | `progress`, `setOnSeekBarChangeListener` |
| “Elegir estrellas” | `RatingBar` | `rating`, `setOnRatingBarChangeListener` |
| “Elegir una categoría” | `Spinner` | `adapter`, `onItemSelectedListener` |
| “Seleccionar mañana o tarde” | `RadioGroup` + `RadioButton` | `setOnCheckedChangeListener` |
| “Añadir al pulsar” | `Button` + campo | `setOnClickListener` |
| “Borrar esta fila” | Botón en `item_usuario.xml` | Callback desde `onBindViewHolder` |
| “Mostrar cuántos hay” | `TextView` | `lista.size.toString()` |
| “Mostrar cuántos están marcados” | `TextView` | `lista.count { it.seleccionado }` |
| “No permitir campo vacío” | `EditText` | `text.toString().trim().isNotEmpty()` |

## 17. Mini recordatorio para el examen

- **ID en XML** → `R.id.nombreDelId`.
- **Leer CheckBox** → `.isChecked`.
- **Escuchar CheckBox** → `.setOnCheckedChangeListener`.
- **Leer SeekBar** → `.progress`.
- **Escuchar SeekBar** → `.setOnSeekBarChangeListener`.
- **Leer EditText** → `.text.toString()`.
- **Pulsar botón** → `.setOnClickListener`.
- **RecyclerView** → `layoutManager` + `adapter`.
- **Crear fila** → `onCreateViewHolder`.
- **Rellenar fila** → `onBindViewHolder`.
- **Número de filas** → `getItemCount()`.
- **MVVM** → datos en ViewModel, observación en Activity y presentación en Adapter.

