package nl.brouwerijdemolen.borefts2013.gui.components

import arrow.core.getOrDefault
import arrow.core.getOrElse
import nl.brouwerijdemolen.borefts2013.gui.Repository
import okio.buffer
import okio.sink
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File

class Exporter : KoinComponent {

    private val repository: Repository by inject()

    suspend fun writeTo(csv: File) {
        if (!csv.parentFile!!.exists()) {
            csv.parentFile!!.mkdir()
        }

        if (csv.exists()) {
            csv.delete()
        }
        csv.createNewFile()

        csv.sink().buffer().use { sink ->
            sink.writeUtf8("brewer,beer,style,festivalSpecial,abv,serving,untappd\n")
            val styles = repository.styles().getOrElse { throw it }
            repository.brewers().getOrDefault { emptyList() }.forEach { brewer ->
                repository.brewerBeers(brewer.id).getOrDefault { emptyList() }.forEach { beer ->
                    val brewerName = brewer.shortName
                    val beerName = beer.name
                    val styleName = styles.firstOrNull { it.id == beer.styleId }?.name
                    val festival = if (beer.festivalBeer) "TRUE" else "FALSE"
                    val abv = beer.abv
                    val serving = beer.serving
                    val untappdLink = if (beer.untappdId != "-1") "https://untappd.com/b/b/${beer.untappdId}" else ""
                    sink.writeUtf8("$brewerName,$beerName,$styleName,$festival,$abv,$serving,$untappdLink\n")
                }
            }
        }


    }

}