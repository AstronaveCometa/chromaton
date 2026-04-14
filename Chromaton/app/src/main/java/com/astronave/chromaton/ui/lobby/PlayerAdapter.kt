package com.astronave.chromaton.ui.lobby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.astronave.chromaton.databinding.ItemPlayerBinding
import com.astronave.chromaton.data.network.Player

class PlayerAdapter : ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(PlayerDiffCallback) {

    // 1. El ViewHolder: Es una clase interna que mantiene las referencias a los ID del XML
    // para no tener que buscarlos con "findViewById" cada vez (ahorra mucha CPU).
    class PlayerViewHolder(private val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.tvPlayerName.text = player.name
        }
    }

    // 2. onCreateViewHolder: Se llama cuando el RecyclerView necesita una nueva "fila" física.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    // 3. onBindViewHolder: Aquí es donde ocurre el "mapeo". Toma los datos de la posición X
    // y los mete en el ViewHolder que creamos arriba.
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 4. PlayerDiffCallback: Esto es el "React.memo" de Android.
    // Compara la lista vieja con la nueva para solo actualizar lo que cambió.
    object PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.player_id == newItem.player_id // Comparamos por ID único
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem // Comparamos el contenido completo
        }
    }
}