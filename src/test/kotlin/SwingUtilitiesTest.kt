import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import javax.swing.SwingUtilities

class SwingUtilitiesTest {
    @Test
    fun `Should process all queued events on the EDT`() {
        val fn = mockk<() -> Unit>(relaxed = true)
        val fnToInvoke = {
            Thread.sleep(2000)
            fn()
        }

        SwingUtilities.invokeLater(fnToInvoke)
        verify(exactly = 0) { fn() }

        flushEdt()
        verify { fn() }
    }
}